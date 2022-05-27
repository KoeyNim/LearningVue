package com.project.vue.admin.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminPostRepository extends JpaRepository<AdminPostEntity, Long>, JpaSpecificationExecutor<AdminPostEntity> {

}