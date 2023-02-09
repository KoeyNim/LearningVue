package com.project.vue.admin.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminPostRepository extends JpaRepository<AdminPostEntity, Long>, JpaSpecificationExecutor<AdminPostEntity> {

	@Override
	/** left outer join 생성 (n+1 방지) */
	@EntityGraph(attributePaths = {"fileEntity"}, type = EntityGraph.EntityGraphType.LOAD)
	Page<AdminPostEntity> findAll(Specification<AdminPostEntity> spec, Pageable pageable);
}