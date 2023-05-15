package com.project.vue.common.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public WebAuthenticationFilter(String url) {
        super(new AntPathRequestMatcher(url, "POST")); // 인증 처리 URL 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("userId");
        String password = request.getParameter("userPwd");

        // 커스텀 토큰 생성
        WebAuthenticationToken authenticationToken = new WebAuthenticationToken(username, password);

        // 인증 매니저에 인증 요청
        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
