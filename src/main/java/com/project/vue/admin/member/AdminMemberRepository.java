package com.project.vue.admin.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminMemberRepository extends JpaRepository<AdminMemberEntity, Long>, JpaSpecificationExecutor<AdminMemberEntity> {

	@Override
	@EntityGraph(attributePaths = {"role"}, type = EntityGraph.EntityGraphType.LOAD) // query left outer join 생성 n+1 방지
	Page<AdminMemberEntity> findAll(Pageable pageable);

}
