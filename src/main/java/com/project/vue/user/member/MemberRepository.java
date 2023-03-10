package com.project.vue.user.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	/** left outer join 생성 (n+1 방지) */
	@EntityGraph(attributePaths = {"role"}, type = EntityGraph.EntityGraphType.LOAD)
	Optional<MemberEntity> findByUserId(String userId);
}
