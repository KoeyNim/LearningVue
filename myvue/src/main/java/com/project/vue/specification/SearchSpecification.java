package com.project.vue.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.project.vue.board.BoardEntity;

public class SearchSpecification {
	
	public static Specification<BoardEntity> searchBoardSpecification(BoardEntity boardEntity) {
		return Specification.<BoardEntity>where((root, query, builder) -> {
			if (StringUtils.isBlank(boardEntity.getSrchVal()))
				return builder.conjunction();
			if (StringUtils.isBlank(boardEntity.getSrchKey()))
				return builder.or(
						builder.like(root.get("title") , "%" + boardEntity.getSrchVal() + "%"),
						builder.like(root.get("content")  , "%" + boardEntity.getSrchVal() + "%"));
			if ("title".equals(boardEntity.getSrchKey()))
				return builder.like(root.get("title"), "%" + boardEntity.getSrchVal() + "%");
			if ("content".equals(boardEntity.getSrchKey()))
				return builder.like(root.get("content"), "%" + boardEntity.getSrchVal() + "%");
			return builder.conjunction();
		});
	}
}
