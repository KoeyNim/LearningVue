package com.project.vue.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vue.role.RoleEnum;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    
	private final MemberRepository memberRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
    
    public MemberEntity save(MemberEntity member) {
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	member.setRole(RoleEnum.USER.getRoleName());
    	return memberRepository.save(member);
    }
    
    public MemberEntity findUserId(String userId) {
    	return memberRepository.findByUserId(userId);
    }
}
