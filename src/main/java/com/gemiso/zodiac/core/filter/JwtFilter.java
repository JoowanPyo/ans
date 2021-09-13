package com.gemiso.zodiac.core.filter;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserRepository;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.core.service.AuthAddService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebFilter(urlPatterns = "/*")
@Order(1)
@Slf4j
public class JwtFilter implements Filter {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthAddService authAddService;

    private List<String> excludedUrls = null;

    @Value("${jwt.auth.secret-key:secret}")
    private String secretKey;

    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {

        String excludePattern = "/swagger-ui/index.html,/swagger-ui/springfox.css,/swagger-ui/swagger-ui-standalone-preset.js"
                + "/swagger-ui/springfox.css,/swagger-ui/swagger-ui-bundle.js,/swagger-ui/springfox.js,/swagger-ui/favicon-32x32.png"
                + ",/swagger-ui/favicon-16x16.png, /swagger-ui/swagger-ui-standalone-preset.js,/swagger-ui/swagger-ui-standalone-preset.js"
                + ",/swagger-ui/favicon-32x32.png,/swagger-ui/favicon-16x16.png,/swagger-resources/configuration/ui"
                + ",/swagger-resources/configuration/security,/swagger-resources,/swagger-ui/swagger-ui-standalone-preset.js"
                + ",/swagger-ui/favicon-32x32.png,/swagger-resources/configuration/ui,/v3/api-docs"
                + ",/swagger-ui/swagger-ui.css,/error,/auth/createToken,/yonhapInternational,/yonhap";
        excludedUrls = Arrays.asList(excludePattern.split(","));

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //param-value URL값 적용[토큰 필터를 거치지 않는다]
        String path = ((HttpServletRequest) request).getServletPath();

        if (!StringUtils.pathEquals(httpServletRequest.getMethod(), "OPTIONS")) {


            if (!excludedUrls.contains(path)) {

                try {
                    final StringBuilder logMessage = new StringBuilder("TOKEN FILTER API - ");

                    final String requestTokenHeader = httpServletRequest.getHeader("Authorization");

                    String jwtToken = null;


                    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
                        //헤더에 있는 Bearer Substring => 토큰값을 빼기 위함.
                        jwtToken = requestTokenHeader.substring(7);
                        Claims claims = Jwts.parser()
                                .setSigningKey(secretKey.getBytes("UTF-8")) // Set Key
                                .parseClaimsJws(jwtToken) // 파싱 및 검증, 실패 시 에러
                                .getBody();

                        Date expiration = claims.get("exp", Date.class);
                        Date nowDate = new Date();

                        //토큰만료 시간 체크
                        if (expiration.before(nowDate)) {
                            //Header header, Claims claims, String message
                            //throw new ExpiredJwtException(null, null, "EXPIRED_ACCESSTOKEN");
                            //((HttpServletResponse) response).sendError(401, "ExpiredJwtException error");
                            httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "EXPIRED_ACCESSTOKEN");
                        } else {
                            logMessage.append(" [TOKEN EXPIRATION TIME:").append(expiration.toString()).append("]");
                        }

                        String userId = claims.get("userId", String.class);


                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("UserId not found. UserId : " + userId));

                        authAddService.authUser = UserDTO.builder().userId(user.getUserId()).build();

                        logMessage.append(" [TOKEN USER ID:").append(user.getUserId().toString()).append("]");

                    } else {
                        httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

                    }

                    logMessage.append(" [RESPONSE STATUS:").append(httpServletResponse.getStatus()).append("]"); //코드를 넘겨줌

                    log.info(String.valueOf(logMessage));

                } catch (Throwable a) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintStream pinrtStream = new PrintStream(out);
                    a.printStackTrace(pinrtStream);
                    log.error(out.toString());
                }
            }
        }

        chain.doFilter(request, response);

    }
}
