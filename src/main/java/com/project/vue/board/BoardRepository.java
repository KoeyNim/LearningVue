package com.project.vue.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity> {

	@Override
	@EntityGraph(attributePaths = {"fileEntity"}, type = EntityGraph.EntityGraphType.LOAD) // query left outer join 생성 n+1 방지
	Page<BoardEntity> findAll(Specification<BoardEntity> spec, Pageable pageable);
	
	@Modifying
	@Query("UPDATE BoardEntity b SET b.count = b.count + 1 WHERE b.id =:id")
	void updateCount(Long id);

}
