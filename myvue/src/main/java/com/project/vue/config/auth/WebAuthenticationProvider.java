package com.project.vue.config.auth;

import java.util.Collections;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@NoArgsConstructor
public class WebAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private MemberRepository memberRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.debug("## AdminAuthenticationProvider");
		log.debug("## authentication {}", authentication);
		
		String userId = authentication.getName();
		String userPwd = (String) authentication.getCredentials();
		log.debug("## userId: {}", userId);
		log.trace("## userPwd: {}", userPwd);
		
//		if (StringUtils.isBlank(userPwd)) {
//			throw new BadCredentialsException("password is Blank");
//		}
		
		MemberEntity findMember = memberRepository.findByUserId(userId);	
		if(ObjectUtils.isEmpty(findMember)) {
			log.debug("1111111111111");
			throw new UsernameNotFoundException("userId is not found. userId=" + userId);
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if(!(passwordEncoder.matches(userPwd, findMember.getUserPwd()))) {    		
			log.debug("## 비밀번호 틀림");
			throw new BadCredentialsException("password is not matched");    		
		}
		
		// Role을 넣지 않으면 Tomcat Thread를 전부 실행함
		return new UsernamePasswordAuthenticationToken(userId, userPwd, Collections.emptyList());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("## CustomUserAuthenticationProvider.supports");
		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
//		return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}

}
