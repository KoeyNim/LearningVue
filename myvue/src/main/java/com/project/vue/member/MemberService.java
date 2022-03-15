package com.project.vue.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    
	private final MemberRepository memberRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
    
    public MemberEntity save(MemberEntity member) {
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	return memberRepository.save(member);
    }
}
