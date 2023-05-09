package com.project.vue.user.member;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;
import com.project.vue.user.payload.MemberSignUpRequest;
import com.project.vue.user.role.RoleEnum;
import com.project.vue.user.role.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional
    public void save(MemberSignUpRequest req) {
		if (isUserId(req.getUserId())) {
			throw new BizException("잘못된 접근입니다.", ErrorCode.BAD_REQUEST);
		}

    	MemberEntity entity = MemberEntity.builder()
    			.userId(req.getUserId())
    			.userPwd(passwordEncoder.encode(req.getUserPwd()))
    			.userName(req.getUserName())
    			.gender(req.getGender())
    			.age(req.getAge())
    			.email(req.getEmail())
    			.phone(req.getPhone())
    			.role(roleRepository.findByRoleKey(RoleEnum.USER.getRoleKey())
    	    			.orElseThrow(() -> new BizException("데이터베이스 에서 해당 권한을 찾을 수 없습니다.", ErrorCode.NOT_FOUND))).build();

    	memberRepository.save(entity);
    }

    public boolean isUserId(String userId) {
    	return memberRepository.findByUserId(userId).isPresent();
    }
}
