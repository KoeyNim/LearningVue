package com.project.vue.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	MemberEntity findByUserId(String userId);
}
