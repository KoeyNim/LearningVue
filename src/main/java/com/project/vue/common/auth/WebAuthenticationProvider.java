package com.project.vue.common.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

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
		log.trace("## WebAuthenticationProvider.authenticate authentication: {}", authentication);
		String userId = authentication.getName();
		String userPwd = (String) authentication.getCredentials();
		log.trace("## userId: {}, userPwd: {}", userId, userPwd);

		MemberEntity memberEntity = memberRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("유저 정보를 찾을 수 없음."));

		if(!(passwordEncoder.matches(userPwd, memberEntity.getUserPwd()))) throw new BadCredentialsException("유효하지 않은 패스워드.");

		return new WebAuthenticationToken(userId, userPwd, memberEntity.getAuthorities());
	}

	/** authenticate Method 진입 전 정상적인 토큰 인지 확인 */
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("## WebAuthenticationProvider.supports Token: {}", authentication.getName());

		/** 같은 클래스 인지 검증(특정 Class가 어떤 클래스/인터페이스를 상속/구현했는지 체크) */
		return AbstractAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
