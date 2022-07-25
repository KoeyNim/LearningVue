package com.project.vue.member;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vue.role.RoleEnum;
import com.project.vue.role.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    
	private final MemberRepository memberRepository;
	
	private final RoleRepository roleRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
    
    public void save(MemberEntity member) {
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	member.setRole(roleRepository.findByRoleKey(RoleEnum.USER.getRoleKey())
    			.orElseThrow(() -> new NoSuchElementException("데이터베이스 에서 해당 권한을 찾을 수 없습니다.")));
    	memberRepository.save(member);
    }
    
    public Optional<MemberEntity> findUserId(String userId) {
    	return memberRepository.findByUserId(userId);
    }
}
