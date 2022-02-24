package com.project.vue.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.project.vue.board.BoardEntity;

public class SearchSpecification {
	
	public static Specification<BoardEntity> searchBoardSpecification(String srchKey, String srchVal) {
		return Specification.<BoardEntity>where((root, query, builder) -> {
			if (StringUtils.isBlank(srchVal))
				return builder.conjunction();
			if (StringUtils.isBlank(srchKey))
				return builder.or(
						builder.like(root.get("title") , "%" + srchVal + "%"),
						builder.like(root.get("content")  , "%" + srchVal + "%"));
			if ("title".equals(srchKey))
				return builder.like(root.get("title"), "%" + srchVal + "%");
			if ("content".equals(srchKey))
				return builder.like(root.get("content"), "%" + srchVal + "%");
			return builder.conjunction();
		});
	}
}
