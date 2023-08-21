package com.project.vue.common.auth;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

		/** 실패시 AuthenticationException(BadCredentialsException) -> WebAuthenticationFailureHandler로 넘김 */
		/** 계정 데이터 체크 */
		MemberEntity memberEntity = memberRepository.findByUserId(userId)
				.orElseThrow(() -> new BadCredentialsException("아이디 및 비밀번호가 다릅니다."));

		/** 비밀번호 체크 */
		if(!passwordEncoder.matches(userPwd, memberEntity.getUserPwd()))
			throw new BadCredentialsException("아이디 및 비밀번호가 다릅니다.");

		/** 권한을 가진 인증 토큰 생성 */
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(userId, userPwd, memberEntity.getAuthorities());

		/** 권한이 없을 경우 인증 거부 */
		if (CollectionUtils.isEmpty(token.getAuthorities())) token.setAuthenticated(false);

		return token;
	}

	/** authenticate Method 진입 전 정상적인 토큰 인지 확인 */
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("## WebAuthenticationProvider.supports Token: {}", authentication.getName());

		/** 같은 클래스 인지 검증(특정 Class가 어떤 클래스/인터페이스를 상속/구현했는지 체크) */
		return AbstractAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
