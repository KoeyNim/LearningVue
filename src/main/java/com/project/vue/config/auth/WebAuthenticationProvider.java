package com.project.vue.config.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("## WebAuthenticationProvider");
		log.debug("## authentication {}", authentication);

		String userId = authentication.getName();
		String userPwd = (String) authentication.getCredentials();
		log.debug("## userId: {}", userId);
		log.trace("## userPwd: {}", userPwd);

		MemberEntity find = memberRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));

		if(!(passwordEncoder.matches(userPwd, find.getUserPwd()))) {
			throw new BadCredentialsException("Bad Credential");
		}

		/** Role을 넣지 않으면 에러 발생 */
		return new UsernamePasswordAuthenticationToken(userId, userPwd, find.getAuthorities());
	}

	/** authenticate Method 진입 전 정상적인 토큰 인지 확인 */
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("## WebAuthenticationProvider.supports");
		/** 같은 클래스 인지 검증 */
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
}
