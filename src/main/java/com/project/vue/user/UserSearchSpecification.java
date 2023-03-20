package com.project.vue.user;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.project.vue.user.board.BoardEntity;
import com.project.vue.user.payload.BoardRequest;

public class UserSearchSpecification {
	/**
	 * Board 검색 조건
	 * @param srch BoardRequest
	 * @return Specification<BoardEntity>
	 */
	public static Specification<BoardEntity> searchBoardSpecification(BoardRequest srch) {
		return Specification.<BoardEntity>where((root, query, builder) -> {
			return builder.and(
					StringUtils.isBlank(srch.getSrchVal()) ? builder.conjunction() : (StringUtils.isBlank(srch.getSrchKey())
									? builder.or(builder.like(root.get("title"), "%" + srch.getSrchVal() + "%"),
											builder.like(root.get("userId"), "%" + srch.getSrchVal() + "%")) : builder.conjunction()),
					"title".equals(srch.getSrchKey()) ? builder.like(root.get("title"), "%" + srch.getSrchVal() + "%") : builder.conjunction(),
					"userId".equals(srch.getSrchKey()) ? builder.like(root.get("userId"), "%" + srch.getSrchVal() + "%") : builder.conjunction());
		});
	}
}
