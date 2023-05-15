package com.project.vue.common.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;
import com.project.vue.user.member.MemberEntity;
import com.project.vue.user.member.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAuthenticationProvider implements AuthenticationProvider {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		log.debug("## WebAuthenticationProvider");
		log.debug("## authentication {}", authentication);
		try {
			String userId = authentication.getName();
			String userPwd = (String) authentication.getCredentials();
			log.trace("## userId: {}, userPwd: {}", userId, userPwd);

			MemberEntity memberEntity = memberRepository.findByUserId(userId)
					.orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));

			if(!(passwordEncoder.matches(userPwd, memberEntity.getUserPwd()))) {
				throw new BadCredentialsException("Bad Credential");
			}

			return new WebAuthenticationToken(userId, userPwd);
//			return new UsernamePasswordAuthenticationToken(userId, userPwd, memberEntity.getAuthorities());
		} catch (AuthenticationException ex) {
			throw new BizException("WebAuthenticationProvider Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	/** authenticate Method 진입 전 정상적인 토큰 인지 확인 */
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("## WebAuthenticationProvider.supports Token: {}", authentication.getName());

		/** 같은 클래스 인지 검증 */
		return WebAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
