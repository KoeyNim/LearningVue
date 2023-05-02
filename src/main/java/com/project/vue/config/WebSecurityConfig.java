package com.project.vue.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.project.vue.common.Constants;
import com.project.vue.common.PathConstants;
import com.project.vue.common.auth.AjaxAuthenticationEntryPoint;
import com.project.vue.common.auth.WebAuthenticationFailureHandler;
import com.project.vue.common.auth.WebAuthenticationSucessHandler;
import com.project.vue.user.role.RoleService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final WebAuthenticationSucessHandler sucessHandler;
	private final WebAuthenticationFailureHandler failureHandler;

	private final RoleService roleService;

	/**
	 * 관리자 URL 
	 */
	@Value("${admin.url}")
	private String adminURL;

    /**
     * 비밀번호 암호화를 위한 Encoder 설정 
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 권한 계층 설정
     * @return {@link DefaultWebSecurityExpressionHandler}
     */
    @Bean
    DefaultWebSecurityExpressionHandler expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        DefaultWebSecurityExpressionHandler securityExpressionHandler = new DefaultWebSecurityExpressionHandler();

        roleHierarchy.setHierarchy(roleService.BuildRoleHierarchy()); // 상위 권한 설정
        securityExpressionHandler.setRoleHierarchy(roleHierarchy); // 권한 계층 커스터마이징
        return securityExpressionHandler;
    }

    /**
     * CORS 설정 (응답 서버에서만 설정)
     * @return {@link CorsConfigurationSource}
     */
    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin(adminURL); // CORS 허용 요청 URL
        config.setAllowedMethods(Arrays.asList( // 허용 Method
        		"GET",    // 조회
        		"POST",   // 생성
        		"PUT",    // 수정
        		"DELETE", // 삭제
        		"HEAD"    // GET과 유사한 방식 웹 서버에서 헤더 정보 이외에는 어떤 데이터도 보내지 않음. (다운 여부 점검, 웹 서버 정보)
        ));
        config.setAllowedHeaders(Arrays.asList( // 허용 Header
        		"Authorization",   // HTTP 요청에서 인증 정보를 전달하는 데 사용되는 헤더
        		"Accept",          // 클라이언트가 지원하는 데이터 유형을 지정하는 데 사용되는 헤더
        		"Cache-Control",   // 캐시 제어 지시자를 포함하는 데 사용되는 헤더
        		"Content-Type",    // 요청 또는 응답 본문의 MIME 유형을 지정하는 데 사용되는 헤더
        		"Origin",          // 요청이 발생한 출처를 지정하는 데 사용되는 헤더
        		"X-CSRF-TOKEN",    // CSRF 토큰을 전달하는 데 사용되는 헤더
        		"X-Requested-With" // AJAX 요청을 구분하는 데 사용되는 헤더
        ));
        /**
         * 요청 자격 증명(예: 쿠키, 인증 헤더 등)을 허용하는지 여부
         * 요청 자격 증명 'Access-Control-Allow-Origin' 옵션이 와일드카드 '*' 가 아니여야 함
         */
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // CORS 허용 후 유지시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // CORS 추가 구성
        source.registerCorsConfiguration("/**", config); // 모든("/**") URL 경로에 대해 CORS 구성이 적용
        return source;
    }

    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .requestMatchers(matchers -> matchers.antMatchers("/static/**", "/error/**"))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                /**
                 * Request Cache 기능을 비활성화
                 * 인증된 사용자의 이전 요청을 저장하여 
                 * 다음 요청 시 인증 없이 접근이 가능하게 하는 기능
                 */
                .requestCache(RequestCacheConfigurer::disable)
                /**
                 * Security Context 기능을 비활성화
                 * 사용자의 보안 컨텍스트 정보를 저장하고
                 * 이를 다른 메서드나 클래스에서 사용할 수 있게 해주는 기능
                 */
                .securityContext(AbstractHttpConfigurer::disable)
                /**
                 * Session Management 기능을 비활성화
                 * 사용자의 세션 정보를 관리하고
                 * 세션 만료 등에 대한 처리를 담당하는 기능
                 */
                .sessionManagement(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers
                		/**
                		 * Cache-Control : 브라우저나 중간 캐시 서버가 콘텐츠를 얼마나 오래 캐시할지 결정
                		 * Cache-Control 헤더를 비활성화
                		 */ 
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable) 
                        /**
                         * X-Frame-Options 헤더를 설정하여 현재 페이지와 
                         * 같은 출처(origin)에서만 프레임(frame) 내부의 콘텐츠를 로드
                         */
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) 
                /**
                 * HTTP Basic 인증 방식을 사용하지 않도록 설정
                 * HTTP Basic : 요청 헤더에 인증 정보를 포함하여 서버에 인증을 요청하는 방식 (보안 취약)
                 */
                .httpBasic().disable()
                .csrf(csrf -> csrf
                		/**
                		 * CSRF(Cross-Site Request Forgery) : 웹 사이트에 대한 요청을 위조하여 수행하는 공격
                		 * CSRF 토큰을 포함하는 쿠키의 httpOnly 속성을 비활성화
                		 */ 
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .cors(cors -> cors.configurationSource(corsConfiguration())) // CORS 설정
                .authorizeRequests(auth -> auth
                        .antMatchers(HttpMethod.GET, "/**").permitAll() // 모든 GET 요청에 대해 로그인을 요구하지 않음
                        .antMatchers(Constants.REQUEST_MAPPING_PREFIX + "/" + PathConstants.MEMBER + "/**").permitAll() // 해당하는 URL 접근에 대해 로그인을 요구하지 않음
                        .expressionHandler(expressionHandler()) // 권한 계층 커스텀 Handler
                        .anyRequest().authenticated()) // 지정하지 않은 모든 요청에 로그인을 요구 (화이트 리스트)
//                		.anyRequest().permitAll() //  지정하지 않은 모든 요청에 로그인을 요구 하지 않음 (블랙 리스트)
                .formLogin(form -> form // 로그인
                        .loginPage("/member-login").permitAll() // 로그인 페이지
                        .loginProcessingUrl(Constants.REQUEST_MAPPING_PREFIX + "/member-login/security") // 로그인 검증 URL
                        .usernameParameter("userId") // 검증시 가지고 갈 아이디 (기본값 username)
                        .passwordParameter("userPwd") // 검증시 가지고 갈 비밀번호 (기본값 password)
                        .successHandler(sucessHandler) // 로그인 성공 Handler
                        .failureHandler(failureHandler)) // 로그인 실패 Handler
                .sessionManagement(session -> session
                        .maximumSessions(1) // 허용 session 갯수, -1인 경우 무제한 세션
                        .expiredUrl("/member-login?expire=true")) // session 만료 시 이동 url
//                        .maxSessionsPreventsLogin(false)) // 동시 로그인 여부(기본값 false) true 일 경우 동시 로그인 차단, false 일 경우 기존 세션 만료
                .logout(logout -> logout // 로그아웃
//    		        	.logoutUrl("/logout") // 로그아웃 url  (기본값 /logout)
                        .logoutSuccessUrl("/member-login") // 성공 url
//    		        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 요청을 처리할 RequestMatcher
                        .clearAuthentication(true) // 인증 객체를 삭제
//                        .invalidateHttpSession(true) //  세션 삭제 여부 (기본값 true) true 일 경우 삭제
                        .deleteCookies("JSESSIONID")) // 삭제할 쿠키의 이름을 지정 (여러 개의 쿠키를 삭제하려면 쉼표로 구분)
                .exceptionHandling(exception -> exception // Exception Handling
                        .authenticationEntryPoint(new AjaxAuthenticationEntryPoint("/member-login"))) // ajax 명령 호출시 검증 클래스
                .build();
    }
}