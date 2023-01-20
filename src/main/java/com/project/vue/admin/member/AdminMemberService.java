package com.project.vue.admin.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.vue.specification.SearchSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

	private final AdminMemberRepository adminMemberRepository;

	public Page<AdminMemberEntity> findAll(Pageable page, AdminMemberRequest srch) {
		return adminMemberRepository.findAll(SearchSpecification.searchAdminMemberSpecification(srch), page);
    }
}
