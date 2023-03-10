package com.gemiso.zodiac.core.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebFilter(urlPatterns = "/*")
@Order(1)
@Slf4j
public class JwtFilter implements Filter {


    //@Autowired
    //private UserRepository userRepository;

    //@Autowired
    //private UserAuthService userAuthService;

   /* @Autowired //강제로 filer error잡는방법, 필터는 서블릿 전단계라 exceptionhandler가 못잡는다.
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;*/

    private List<String> excludedUrls = null;

    @Value("${jwt.auth.secret-key:secret}")
    private String secretKey;

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {

        ///users/createuser - 사용자등록 최초시
        String excludePattern = "/swagger-ui/index.html,/swagger-ui/springfox.css,/swagger-ui/swagger-ui-standalone-preset.js,/swagger-ui/null/swagger-resources/configuration/ui"
                + ",/swagger-ui/springfox.css,/swagger-ui/swagger-ui-bundle.js,/swagger-ui/springfox.js,/swagger-ui/favicon-32x32.png"
                + ",/swagger-ui/favicon-16x16.png, /swagger-ui/swagger-ui-standalone-preset.js,/swagger-ui/swagger-ui-standalone-preset.js"
                + ",/swagger-ui/favicon-32x32.png,/swagger-ui/favicon-16x16.png,/swagger-resources/configuration/ui"
                + ",/swagger-resources/configuration/security,/swagger-resources,/swagger-ui/swagger-ui-standalone-preset.js"
                + ",/swagger-ui/favicon-32x32.png,/swagger-resources/configuration/ui,/v3/api-docs,/swagger-ui/swagger-ui.css,/error"
                + ",/swagger-ui/index.html/swagger-resources,/swagger-ui/index.html/swagger-resources/configuration/ui"
                + ",/swagger-ui/index.html/swagger-resources/configuration/security,/auth/login,/auth/againlogin,/auth/logout"
                + ",/auth/loginmobile,/auth//mobilelogout"
                + ",/swagger-ui/null/swagger-resources/configuration/security,/swagger-ui/null/swagger-resources"
                + ",/yonhapinternational,/yonhapinternational/aptn,/yonhapinternational/reuter"
                + ",/yonhap,/yonhap/file"
                + ",/yonhapphoto/extend"
                + ",/ytnrundown"
                + ",/interface,/interface/dailypgm,/interface/cuesheet,/interface/code,/interface/mediatransrate,/interface/getmstlistservice"
                + ",/interface/getcuesheetservice,/interface/pstakerlist,/interface/pstaker,/interface/takerrefresh"
                + ",/interface/mediatransfer/updatestate,/interface/cuestcdupdate,/interface/takersetcue"
                + ",/interface/smamfindcue,/interface/smamfindallcue,/interface/homepagecd,/interface/users,/interface/user,/interface/promptersetcue"
                + ",/nod,/interface/getcuesheetservice/prompter,/interface/article";
        excludedUrls = Arrays.asList(excludePattern.split(","));

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ExpiredJwtException, ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //param-value URL값 적용[토큰 필터를 거치지 않는다]
        String path = ((HttpServletRequest) request).getServletPath();

        if (!StringUtils.pathEquals(httpServletRequest.getMethod(), "OPTIONS")) {

            //인터페이스 필터정보
            String interfacePath = "/interface/cuestcdupdate" + path.substring(path.lastIndexOf("/"));
           /* if (interfacePath.equals(path)) {
                chain.doFilter(request, response);*/

                if (excludedUrls.contains(path) == false && interfacePath.equals(path) == false) {

                    try {
                        final StringBuilder logMessage = new StringBuilder("TOKEN FILTER API - ");

                        final String requestTokenHeader = httpServletRequest.getHeader("Authorization");

                        String jwtToken = null;


                        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {

                            //헤더에 있는 Bearer Substring => 토큰값을 빼기 위함.
                            jwtToken = requestTokenHeader.substring(6);
                            Claims claims = Jwts.parser()
                                    .setSigningKey(secretKey.getBytes("UTF-8")) // Set Key
                                    .parseClaimsJws(jwtToken) // 파싱 및 검증, 실패 시 에러
                                    .getBody();


                            Date expiration = claims.get("exp", Date.class);
                            Date nowDate = new Date();

                            //토큰만료 시간 체크
                            if (expiration.before(nowDate)) {
                                //Header header, Claims claims, String message
                                //throw new UserExpiredJwtException(null, null, "EXPIRED_ACCESSTOKEN");
                                //((HttpServletResponse) response).sendError(401, "UserExpiredJwtException error");
                                httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "EXPIRED_ACCESSTOKEN");
                            } else {
                                logMessage.append(" [TOKEN EXPIRATION TIME:").append(expiration.toString()).append("]");
                            }

                            //SecurityContextHolder.getContext().setAuthentication();

                            /*String userId = claims.get("userId", String.class);


                            User user = userRepository.findById(userId)
                                    .orElseThrow(() -> new ResourceNotFoundException("UserId not found. UserId : " + userId));

                            UserDTO userDTO = UserDTO.builder().userId(user.getUserId()).build();

                            userAuthService.authUser= userDTO;
                            //userAuthService.authUser = user;

                            *//*********** ip 가져오기 ************//*

                            String userIp = httpServletRequest.getRemoteAddr();
                            userAuthService.userip = userIp;

                            *//*********** ip 가져오기 ************/

                            //logMessage.append(" [TOKEN USER ID:").append(user.getUserId().toString()).append("]");

                        } else {
                            httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "토큰이 없거나 토큰정보가 정확하지 않습니다.");
                            //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                        }

                        logMessage.append(" [RESPONSE STATUS:").append(httpServletResponse.getStatus()).append("]"); //코드를 넘겨줌

                        //log.info(String.valueOf(logMessage));


                    } catch (ExpiredJwtException ex) {
                        log.error(ex.toString());

                   /* //에러리스폰스 작성.
                    ApiErrorResponse.Error error =
                            new ApiErrorResponse.Error(ApiErrorResponse.ErrorCodes.EXPIRED_ACCESSTOKEN, ex.getLocalizedMessage());
                    ApiErrorResponse apiErrorResponse = new ApiErrorResponse(error, HttpStatus.UNAUTHORIZED);

                    //에러리스폰스를 json형식으로
                    ObjectMapper mapper = new ObjectMapper();

                    PrintWriter out = httpServletResponse.getWriter();
                    out.println(mapper.writeValueAsString(apiErrorResponse));
                    out.flush();*/
                        httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "EXPIRED_ACCESSTOKEN");
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

                        /*resolver.resolveException(httpServletRequest, httpServletResponse, null, ex);//filter error handling방법*/
                    } /*catch (Throwable a) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintStream pinrtStream = new PrintStream(out);
                    a.printStackTrace(pinrtStream);
                    log.error(out.toString());
                    httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "EXPIRED_ACCESSTOKEN");

                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }*/
                }
            }
        chain.doFilter(request, response);

    }
}
