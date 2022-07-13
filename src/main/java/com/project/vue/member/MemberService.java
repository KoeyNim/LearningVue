package com.project.vue.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vue.role.RoleEntity;
import com.project.vue.role.RoleEnum;
import com.project.vue.role.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    
	private final MemberRepository memberRepository;
	
	private final RoleRepository roleRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
    
    public MemberEntity save(MemberEntity member) {
    	RoleEntity roleEntity = new RoleEntity();
    	roleEntity.setRoleKey(RoleEnum.USER.getRoleKey());
    	
    	log.debug("member {}", member);
    	log.debug("roleEntity {}", roleEntity);
    	
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	member.setRole(roleRepository.findByRoleKey(RoleEnum.USER.getRoleKey()));
    	log.debug("member {}", member);
    	return memberRepository.save(member);
    }
    
    public MemberEntity findUserId(String userId) {
    	return memberRepository.findByUserId(userId);
    }
}
