package com.project.vue.common.auth;
/*
import java.util.Collection;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class WebAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private String userId;
	private String userPwd;

	/** 검증되지 않은 인증 객체 
	public WebAuthenticationToken(String userId, String userPwd) {
		this(userId, userPwd, null);
	}

	/** 인증 객체 (권한 여부에 따라 인증 여부 변경) 
	public WebAuthenticationToken(String userId, String userPwd, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.userId = userId;
		this.userPwd = userPwd;
		super.setAuthenticated(ObjectUtils.isNotEmpty(authorities) && true); // 인증 여부
	}

	@Override
	public Object getCredentials() {
		return this.userPwd;
	}

	@Override
	public Object getPrincipal() {
		return this.userId;
	}

	/**
	 *	비밀번호 삭제

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.userPwd = null;
	}
}
*/

//2023-08-21 사용하지 않음.
