package com.project.vue.common.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtTokenInfo {

	/** 인증 Type */
    private String grantType;
    /** API 인증 Token */
    private String accessToken;
    /** 토큰 재발급을 위한 토큰 */
    private String refreshToken;
}

