package com.project.vue.member;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    public void logIn(MemberEntity member) {
    	log.debug("@!#@!@#!@{}",member);
    	MemberEntity findMember = memberRepository.findByUserId(member.getUserId());
    	if(ObjectUtils.isNotEmpty(findMember)) {
    		log.debug("찾기성공");
    		if(passwordEncoder.matches(member.getUserPwd(), findMember.getUserPwd())) {
    			log.debug("매칭성공");
    		}
    	}
    	log.debug("실패");
    }

//    public MemberEntity loadUserByUserId(String userid) throws UsernameNotFoundException {
//        return memberRepository.findByUserId(userid)
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//    }
    
    public MemberEntity save(MemberEntity member) {
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	return memberRepository.save(member);
    }
}
