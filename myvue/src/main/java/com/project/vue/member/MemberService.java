package com.project.vue.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    
	private final MemberRepository memberRepository;
    
    public MemberEntity save(MemberEntity member) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	return memberRepository.save(member);
    }
}
