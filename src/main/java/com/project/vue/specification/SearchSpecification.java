package com.project.vue.specification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.project.vue.admin.member.AdminMemberEntity;
import com.project.vue.admin.member.AdminMemberRequest;
import com.project.vue.admin.post.AdminPostEntity;
import com.project.vue.admin.post.AdminPostRequest;
import com.project.vue.board.BoardEntity;
import com.project.vue.board.BoardRequest;

public class SearchSpecification {

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

	/**
	 * Admin Post 검색 조건
	 * @param srch AdminPostRequest
	 * @return Specification<AdminPostEntity>
	 */
	public static Specification<AdminPostEntity> searchAdminPostSpecification(AdminPostRequest srch) {
		return Specification.<AdminPostEntity>where((root, query, builder) -> {
			if (StringUtils.isBlank(srch.getSrchVal())) return builder.conjunction();
			if (StringUtils.isBlank(srch.getSrchKey()))
				return builder.or( builder.like(root.get("title"), "%" + srch.getSrchVal() + "%"),
								   builder.like(root.get("userId"), "%" + srch.getSrchVal() + "%"));
			if ("title".equals(srch.getSrchKey())) return builder.like(root.get("title"), "%" + srch.getSrchVal() + "%");
			if ("userId".equals(srch.getSrchKey())) return builder.like(root.get("userId"), "%" + srch.getSrchVal() + "%");
			return builder.conjunction();
		});
	}

	/**
	 * Admin Member 검색 조건
	 * @param srch AdminPostRequest
	 * @return Specification<AdminMemberEntity>
	 */
	public static Specification<AdminMemberEntity> searchAdminMemberSpecification(AdminMemberRequest srch) {
		return Specification.<AdminMemberEntity>where((root, query, builder) -> {
			if (StringUtils.isBlank(srch.getSrchVal())) return builder.conjunction();
			if (StringUtils.isBlank(srch.getSrchKey()))
				return builder.or( builder.like(root.get("userName"), "%" + srch.getSrchVal() + "%"),
								   builder.like(root.get("userId"), "%" + srch.getSrchVal() + "%"));
			if ("userName".equals(srch.getSrchKey())) return builder.like(root.get("userName"), "%" + srch.getSrchVal() + "%");
			if ("userId".equals(srch.getSrchKey())) return builder.like(root.get("userId"), "%" + srch.getSrchVal() + "%");
			return builder.conjunction();
		});
	}
}
