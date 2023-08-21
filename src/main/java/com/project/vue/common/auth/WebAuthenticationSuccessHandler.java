package com.project.vue.common.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.project.vue.common.auth.jwt.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	private final JwtService jwtService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.debug("## AdminAuthenticationSuccessHandler");

		/* Ajax를 사용하므로 동작하지 않음.
		super.setDefaultTargetUrl("/board");
		*/
		
//		jwtService.generateToken(authentication, memberEntity.getMemberUid()); // TODO 08.11 작업중

		// 세션처리, 예외처리 필요할 경우 구현
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
