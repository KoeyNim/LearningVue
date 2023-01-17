package com.project.vue.member;

import java.util.NoSuchElementException;

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

    public void save(MemberSignUpRequest req) {
    	MemberEntity entity = MemberEntity.builder()
    			.userId(req.getUserId())
    			.userPwd(passwordEncoder.encode(req.getUserPwd()))
    			.userName(req.getUserName())
    			.gender(req.getGender())
    			.age(req.getAge())
    			.email(req.getEmail())
    			.phone(req.getPhone())
    			.role(roleRepository.findByRoleKey(RoleEnum.USER.getRoleKey())
    	    			.orElseThrow(() -> new NoSuchElementException("데이터베이스 에서 해당 권한을 찾을 수 없습니다."))).build();

    	memberRepository.save(entity);
    }

    public boolean findByUserid(String userId) {
    	return memberRepository.findByUserId(userId).isPresent();
    }
}
