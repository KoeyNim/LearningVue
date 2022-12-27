package com.project.vue.config.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AjaxAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public AjaxAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) {
		log.debug("@@ AjaxAuthenticationEntryPoint");
		String header = req.getHeader("X-Requested-With");
		log.debug("@@ AjaxAuthenticationEntryPoint header : {}", header);
		try {
			if(StringUtils.isNotBlank(header) && StringUtils.equals("XMLHttpRequest", header)) {
				Map<String, Object> result = new HashMap<>();
				result.put("success", false);
				result.put("errType", header);
				result.put("message", this.buildRedirectUrlToLoginPage(req, res, exception));
				
				res.setContentType("application/json");
				res.setStatus(HttpStatus.UNAUTHORIZED.value());
				res.getOutputStream().write(new ObjectMapper().writeValueAsBytes(result));
				return;
			}
			super.commence(req, res, exception);
		} catch (ServletException | IOException e) {
			throw new RuntimeException(e); // TODO Exception
		}
	}
}