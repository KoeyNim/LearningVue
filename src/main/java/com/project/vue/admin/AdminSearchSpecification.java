package com.project.vue.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.project.vue.admin.board.AdminBoardEntity;
import com.project.vue.admin.member.AdminMemberEntity;
import com.project.vue.admin.payload.AdminBoardRequest;
import com.project.vue.admin.payload.AdminMemberRequest;

public class AdminSearchSpecification {
	/**
	 * Admin Board 검색 조건
	 * @param srch AdminBoardRequest
	 * @return Specification<AdminBoardEntity>
	 */
	public static Specification<AdminBoardEntity> searchAdminBoardSpecification(AdminBoardRequest srch) {
		return Specification.<AdminBoardEntity>where((root, query, builder) -> {
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
	 * @param srch AdminMemberRequest
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
