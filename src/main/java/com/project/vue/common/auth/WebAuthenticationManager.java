package com.project.vue.common.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebAuthenticationManager implements AuthenticationManager {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("## WebAuthenticationManager");
		log.trace("## WebAuthenticationManager.authenticate authentication: {}", authentication);
		return authentication;
	}

}
