package com.project.vue.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.vue.common.Constants;
import com.project.vue.config.auth.WebAuthenticationFailureHandler;
import com.project.vue.config.auth.WebAuthenticationProvider;
import com.project.vue.config.auth.WebAuthenticationSucessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final WebAuthenticationSucessHandler authenticationSucessHandler;
	
	private final WebAuthenticationFailureHandler authenticationFailureHandler;
	
	private final WebAuthenticationProvider authenticationProvider;
	
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
//    @Bean public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() { 
//    	return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher()); 
//    }

	
    // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
//    	http.headers(headers -> headers.cacheControl(cache -> cache.disable()));
    	
    	// 기본 Login Form 제거
    	http.httpBasic().disable();
    	// 접근 권한
        http
//        .cors().disable() // cors 무력화
//        .csrf() // csrf 무력화
//        	  .ignoringAntMatchers("/login", "/login/**", "/logout")
//        	  .disable()
        .authorizeRequests()
        	  .antMatchers(Constants.REQUEST_MAPPING_PREFIX+"/board/**").authenticated()
	          .antMatchers("/board/**").authenticated() // board 요청에 대해서는 로그인을 요구
//	          .antMatchers("/admin/**").hasRole("ADMIN")
//	          .antMatchers("/user/**").hasRole("USER")
	          .anyRequest().permitAll() // 나머지 요청에 대해서는 로그인을 요구하지 않음.
        
        // 로그인
	    .and().formLogin()
	          .loginPage("/login") // 로그인 페이지
	          .loginProcessingUrl(Constants.REQUEST_MAPPING_PREFIX + "/login/security") // 로그인 검증 url
              .usernameParameter("userId") // 검증시 가지고 갈 아이디
              .passwordParameter("userPwd") // 검증시 가지고 갈 비밀번호
	          .successHandler(authenticationSucessHandler) // 로그인 성공 핸들러
	          .failureHandler(authenticationFailureHandler) // 로그인 실패 핸들러
	          //.failureForwardUrl("/") failureHandler 보다 우선시 되므로 failureHandler를 사용하고 싶을 경우 사용하지 말것
        	  .permitAll();
        
//        http.securityContext().securityContextRepository(new NullSecurityContextRepository()); // 로그인된 사용자 삭제시 에러 표시
       
        // 인증 필터
//        .and().addFilterBefore(new WebAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        http.sessionManagement()
    	    .maximumSessions(1) // 허용 session 갯수, -1인 경우 무제한 세션
    	    .expiredUrl("/login?expire=true") // session 만료 시 이동 url
    	    .maxSessionsPreventsLogin(false); // true일 경우 동시로그인 차단, false일 경우 기존 세션 만료
//        .and().sessionFixation().changeSessionId(); // 세션 고정 보호
        
        // 로그아웃
        http.logout()
        	.logoutUrl("/logout")
        	.logoutSuccessUrl("/login") // 로그아웃 성공 url
//        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 실행 주소
        	.clearAuthentication(true) // 로그아웃시 인증정보 삭제
        	.invalidateHttpSession(true).deleteCookies("JSESSIONID"); // 로그아웃 시 세션 삭제, 쿠키 제거
    }
    
    // 사용자 정보 검증 Provider Method
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authenticationProvider);
    }

}