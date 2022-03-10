package com.project.vue.config.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAuthenticationSucessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.debug("## AdminAuthenticationSuccessHandler");
		log.debug("## authentication.getPrincipal(): {}", authentication.getPrincipal());
		log.debug("## authentication.getCredentials(): {}", authentication.getCredentials());
		log.debug("## authentication.isAuthenticated(): {}", authentication.isAuthenticated());
		log.debug("## request.getSession(): {}", request.getSession(false));
		
		// 에러 세션 클리어
//		clearAuthenticationAttributes(request);
		HttpSession session = request.getSession();
		session.setAttribute("userId", authentication.getPrincipal());

		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("message", "로그인 성공");
		response.getOutputStream().write(
				new ObjectMapper().writeValueAsBytes(result));
		
	}
	
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		log.debug("login session exsist {}", session);
		if (session == null) return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}


}
