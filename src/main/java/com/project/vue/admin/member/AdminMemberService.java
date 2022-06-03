package com.project.vue.admin.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    
	private final AdminMemberRepository memberRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;
    
    public AdminMemberEntity save(AdminMemberEntity member) {
    	member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
    	return memberRepository.save(member);
    }
    
    public Page<AdminMemberEntity> findAll(
    		int pageIndex, int pageSize,
    		String sortKey, String order,
    		String srchKey, String srchVal) {
    	PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, 
    			Sort.by(StringUtils.isBlank(order)   ? Direction.DESC : Direction.valueOf(order),
    					StringUtils.isBlank(sortKey) ? "id"   : sortKey));
		return memberRepository.findAll(pageRequest);
    }
}
