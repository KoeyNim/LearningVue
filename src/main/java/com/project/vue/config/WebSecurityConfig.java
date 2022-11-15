package com.project.vue.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.vue.common.Constants;
import com.project.vue.config.auth.WebAuthenticationFailureHandler;
import com.project.vue.config.auth.WebAuthenticationSucessHandler;
import com.project.vue.role.RoleService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final WebAuthenticationSucessHandler authenticationSucessHandler;

	private final WebAuthenticationFailureHandler authenticationFailureHandler;

	private final RoleService roleHierarchyService;

//	private final WebAuthenticationProvider authenticationProvider;

	//비밀번호 암호화를 위한 Encoder 설정
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() { 
//    	return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher()); 
//    }

    // CORS 허용 적용 (응답 서버에서만 설정)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000"); // CORS 허용 요청 url
        config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE")); // 허용 Method
        config.setAllowedHeaders(Arrays.asList( // 허용 Header
                "Authorization",
                "Accept",
                "Cache-Control",
                "Content-Type",
                "Origin",
                "ajax",   // $.ajaxSetup({ // front 에서 ajax 세팅 시 설정
			              // crossDomain: true,
			              // xhrFields: {withCredentials: true}});
                "x-csrf-token",
                "x-requested-with",
                "Content-Type"
        ));
        config.setAllowCredentials(true); // 요청 자격증명 'Access-Control-Allow-Origin' 의 옵션이 와일드카드 '*' 가 아니여야 함
        config.setMaxAge(3600L); // CORS 허용 후 유지시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 정적 자원에 대해서는 Security 설정을 적용하지 않음. WebSecurity가 HttpSecurity 보다 우선순위 이다.
    @Override
    public void configure(WebSecurity web) throws Exception {
//    	web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 폴더명이 지정되어 있음.
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/error"); // 작동하지 않음
    }

    // 권한 계층 설정 Bean
    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
    	RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    	DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();

    	// 상위 권한 설정
    	roleHierarchy.setHierarchy(roleHierarchyService.BuildAllHierarchy());
    	// 권한 계층 커스터마이징
    	securityExpressionHandler.setRoleHierarchy(roleHierarchy);
    	return securityExpressionHandler;
    }

	@Override
    protected void configure(HttpSecurity http) throws Exception {
//    	http.headers(headers -> headers.cacheControl(cache -> cache.disable()));
    	http.httpBasic().disable() // 기본 Login Form 제거
    	.cors().configurationSource(corsConfigurationSource()); // CORS 설정
//        .cors().disable() // CORS 무력화
        http.csrf() // CSRF 설정 security, ajax, form 중에 적용해야 하는 곳과 GET Method 제외 나머지 Method 에만 적용해야하는 이유 등 추가 설명 필요 
        	  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()); // CSRF 토큰자동생성
//  	  	  .ignoringAntMatchers("/login", "/login/**", "/logout") // CSRF 예외처리
//        	  .disable() // CSRF 미적용

        http.authorizeRequests()
//        	  .antMatchers(Constants.REQUEST_MAPPING_PREFIX+"/board/**").authenticated()
//	          .antMatchers("/board/**").authenticated() // board 요청에 대해서는 로그인을 요구
//	          .antMatchers("/admin/**").hasRole("ADMIN")
//	          .antMatchers("/user/**").hasRole("USER")
	          .antMatchers(HttpMethod.GET,"/").permitAll() // 로그인을 요구하지 않음
        	  .antMatchers(Constants.REQUEST_MAPPING_PREFIX+"/board/**").hasRole("USER2")
        	  .antMatchers("/api/**").permitAll()
	          .antMatchers(HttpMethod.GET,"/member-signup").permitAll()
	          .antMatchers(Constants.REQUEST_MAPPING_PREFIX+"/member/member-signup").permitAll()
//	          .anyRequest().permitAll() // 나머지 요청에 대해서는 로그인을 요구하지 않음.
	          .expressionHandler(expressionHandler()) // 권한 계층 커스텀 Method
	          .anyRequest().authenticated() // 모든 요청에 로그인을 요구

        // 로그인 인증
	    .and().formLogin()
	          .loginPage("/member-login") // 로그인 페이지
	          .loginProcessingUrl(Constants.REQUEST_MAPPING_PREFIX + "/member-login/security") // 로그인 검증 url
              .usernameParameter("userId") // 검증시 가지고 갈 아이디 기본값 : username
              .passwordParameter("userPwd") // 검증시 가지고 갈 비밀번호 기본값 : password
	          .successHandler(authenticationSucessHandler) // 로그인 성공 핸들러
	          .failureHandler(authenticationFailureHandler) // 로그인 실패 핸들러
	          //.failureForwardUrl("/") failureHandler 보다 우선시 되므로 failureHandler를 사용하고 싶을 경우 사용하지 말것
        	  .permitAll();
        // 인가 필터
//        http.addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);
//        http.securityContext().securityContextRepository(new NullSecurityContextRepository()); // 로그인된 사용자 삭제시 에러 표시
        // 인증 필터
//        .and().addFilterBefore(new WebAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement()
    	    .maximumSessions(1) // 허용 session 갯수, -1인 경우 무제한 세션
    	    .expiredUrl("/member-login?expire=true") // session 만료 시 이동 url
    	    .maxSessionsPreventsLogin(false); // true일 경우 동시로그인 차단, false일 경우 기존 세션 만료
//        .and().sessionFixation().changeSessionId(); // 세션 고정 보호
        // 로그아웃
        http.logout()
//        	.logoutUrl("/logout") // 로그아웃 url  기본값 : /logout
        	.logoutSuccessUrl("/member-login") // 로그아웃 성공 url
//        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 실행 주소
        	.clearAuthentication(true) // 로그아웃시 인증정보 삭제
        	.invalidateHttpSession(true).deleteCookies("JSESSIONID"); // 로그아웃 시 세션 삭제, 쿠키 제거
    }
    // 사용자 정보 검증 Provider Method 자동으로 잡아주므로 사용할 필요가 없는 것 같음.
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//    	auth.authenticationProvider(authenticationProvider);
//    }
}