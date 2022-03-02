package com.project.vue.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
	
    // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//        	  .antMatchers(Constants.REQUEST_MAPPING_PREFIX+"/board/**").authenticated()
//	          .antMatchers("/board/**").authenticated() // board 요청에 대해서는 로그인을 요구
	//          .antMatchers("/admin/**").hasRole("ADMIN")
	//          .antMatchers("/user/**").hasRole("USER")
	          .anyRequest().permitAll() // 나머지 요청에 대해서는 로그인을 요구하지 않음.
	          .and()
	        .formLogin() // 로그인
	          .loginPage("/signin") // 로그인 페이지
	          .successForwardUrl("/board") // 로그인 성공 url
	          .failureForwardUrl("/") // 로그인 실패 url
        	  .permitAll();
//        	  .and();
//        	  .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
