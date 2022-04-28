package com.project.vue.config.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class WebAuthenticationToken extends AbstractAuthenticationToken {
	
	private String userId;
	private String userPwd;

	public WebAuthenticationToken(String userId, String userPwd, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.userId = userId;
		this.userPwd = userPwd;
	}
	
    public WebAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

	@Override
	public Object getCredentials() {
		return this.userPwd;
	}

	@Override
	public Object getPrincipal() {
		return this.userId;
	}

}
