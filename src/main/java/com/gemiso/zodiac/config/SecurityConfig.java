package com.gemiso.zodiac.config;

import com.gemiso.zodiac.core.filter.JwtFilter;
import com.gemiso.zodiac.core.filter.ReqRespDumpFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.Filter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //private final UserDetailsService userDetailsService;
    //private final PasswordEncoder passwordEncoder; //패스워드 암호화 interface
    //private final JWTBuilder jwtBuilder;
    //private final JWTParser jwtParser;

/*    @Override //코드를 통해서 직접 인증 매니저를 설정할 때 사용
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }*/

/*    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()      // csrf 비활성화
                .formLogin().disable() //기본 로그인 페이지 없애기
                .authorizeRequests().antMatchers("*").permitAll();
                //.requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and().cors().and();

                //.antMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**, /v3/api-docs").permitAll();
                //.and()
                //.addFilterBefore(new JwtFilter(), WebAsyncManagerIntegrationFilter.class);

        //http.addFilterBefore(new JwtFilter(), WebAsyncManagerIntegrationFilter.class);


        //http.addFilterBefore(new JwtFilter(), WebAsyncManagerIntegrationFilter.class);

        //http.addFilterAfter(new ReqRespDumpFilter(), WebAsyncManagerIntegrationFilter.class);
        //http.addFilter(new ReqRespDumpFilter());
        //http.addFilterBefore(new ReqRespDumpFilter(), WebAsyncManagerIntegrationFilter.class);

      /*  ApiAuthenticationFilter apiAuthenticationFilter = new ApiAuthenticationFilter(authenticationManagerBean(), jwtBuilder);
        apiAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/login"); // 로그인 요청 URI를 정의해준다.
        apiAuthenticationFilter.setUsernameParameter("identifier"); //를 통해 폼으로 넘어오는 사용자 아이디 변수값을 설정한다.
        apiAuthenticationFilter.setPasswordParameter("password");   //를 통해 폼으로 넘어오는 사용자 패스워드 변수값을 설정한다.

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests() //인증이 필요한 자원들을 설정 할 수 있다.
                .antMatchers("/api/v1/auth/login", "api/v1/users")
                .permitAll(); // 전체 권한 주기*/
        // 일부 권한 주기
//        http.authorizeRequests().antMatchers("/api/v1/auth/login/**", "api/v1/auth/refresh-token/**").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/v1/users/**").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();

        // 인증 필터
      /*  http.addFilter(apiAuthenticationFilter);
        // 인가 필터
        http.addFilterBefore(new ApiAuthorizationFilter(jwtParser), UsernamePasswordAuthenticationFilter.class);*/
    }

    @Override
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**, /v3/api-docs");
    }

   /* @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

}
