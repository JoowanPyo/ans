package com.gemiso.zodiac.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = "/*")
@Order(2)
@Slf4j
public class InterfaceFilter implements Filter {

    @Value("${security-Key:secret}")
    private String secretKey;

    private List<String> excludedUrls = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String excludePattern = ",/interface,/interface/dailypgm,/interface/cuesheet,/interface/code,/interface/mediatransrate" +
                ",/interface/getmstlistservice,/interface/getcuesheetservice,/interface/pstakerlist,/interface/pstaker" +
                ",/interface/takerrefresh,/interface/mediatransfer/updatestate,/interface/cuestcdupdate,/interface/takersetcue" +
                ",/interface/smamfindcue,/interface/smamfindallcue,/interface/homepagecd,/interface/users,/interface/user,/nod" +
                ",/interface/getcuesheetservice/prompter";
        excludedUrls = Arrays.asList(excludePattern.split(","));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //param-value URL값 적용
        String path = ((HttpServletRequest) request).getServletPath();

        //인터페이스 필터정보
        String interfacePath = "/interface/cuestcdupdate"+path.substring(path.lastIndexOf("/"));


            if (excludedUrls.contains(path) || interfacePath.equals(path)) {

                final String requestTokenHeader = httpServletRequest.getHeader("securityKey");

                if (secretKey.equals(requestTokenHeader) == false || requestTokenHeader == null || requestTokenHeader.trim().isEmpty()) {
                    log.error("헤더에 Security key 값이 값이 잘못 되었습니다." + requestTokenHeader);
                    httpServletResponse.sendError(httpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                }
            }

        chain.doFilter(request, response);
    }
}
