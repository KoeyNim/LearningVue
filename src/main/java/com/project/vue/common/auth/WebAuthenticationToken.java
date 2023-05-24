package com.project.vue.common.auth;

import java.util.Collection;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class WebAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private String userId;
	private String userPwd;

	public WebAuthenticationToken(String userId, String userPwd) {
		this(userId, userPwd, null);
	}

	public WebAuthenticationToken(String userId, String userPwd, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.userId = userId;
		this.userPwd = userPwd;
		super.setAuthenticated(ObjectUtils.isNotEmpty(authorities) && true);
	}

	@Override
	public Object getCredentials() {
		return this.userPwd;
	}

	@Override
	public Object getPrincipal() {
		return this.userId;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.userPwd = null;
	}
}
