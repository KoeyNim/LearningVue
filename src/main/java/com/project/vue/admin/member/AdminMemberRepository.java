package com.project.vue.admin.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

public interface AdminMemberRepository extends JpaRepository<AdminMemberEntity, String>, JpaSpecificationExecutor<AdminMemberEntity> {

	@Override
	/** left outer join 생성 (n+1 방지) */
	@EntityGraph(attributePaths = {"role"}, type = EntityGraph.EntityGraphType.LOAD)
	Page<AdminMemberEntity> findAll(@Nullable Specification<AdminMemberEntity> spec, Pageable pageable);
}
