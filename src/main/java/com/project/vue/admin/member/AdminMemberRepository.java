package com.project.vue.admin.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.project.vue.admin.post.AdminPostEntity;

public interface AdminMemberRepository extends JpaRepository<AdminMemberEntity, Long>, JpaSpecificationExecutor<AdminPostEntity> {

}
