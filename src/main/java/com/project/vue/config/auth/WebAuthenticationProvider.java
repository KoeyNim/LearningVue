package com.project.vue.config.auth;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.vue.member.MemberEntity;
import com.project.vue.member.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAuthenticationProvider implements AuthenticationProvider {

	private final MemberRepository memberRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("## AdminAuthenticationProvider");
		log.debug("## authentication {}", authentication);
		
		String userId = authentication.getName();
		String userPwd = (String) authentication.getCredentials();
		log.debug("## userId: {}", userId);
		log.trace("## userPwd: {}", userPwd);
		
		MemberEntity findMember = memberRepository.findByUserId(userId);
		log.debug("## findMember: {}", findMember);
		
		if(userId.isEmpty()) {
			throw new UsernameNotFoundException("아이디를 입력해주세요.");
		}
		
		if(userPwd.isEmpty()) {
			throw new BadCredentialsException("비밀번호를 입력해주세요.");    		
		}
		
		if(ObjectUtils.isEmpty(findMember)) {
			throw new UsernameNotFoundException("찾을 수 없는 아이디 입니다.");
		}
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(!(passwordEncoder.matches(userPwd, findMember.getUserPwd()))) {
			throw new BadCredentialsException("비밀번호가 다릅니다. 다시 입력해주세요.");    		
		}
		
		// Role을 넣지 않으면 Tomcat Thread를 전부 실행함
		return new UsernamePasswordAuthenticationToken(userId, userPwd, findMember.getAuthorities());
	}
	
	// authenticate Method 진입 전 정상적인 토큰 인지 확인
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("## CustomUserAuthenticationProvider.supports");
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class); // 동일클래스 인지 검증
	}

}
