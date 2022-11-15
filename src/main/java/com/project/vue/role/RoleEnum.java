package com.project.vue.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

	ADMIN("ROLE_ADMIN", "어드민"),
	MANAGER("ROLE_MANAGER", "매니저"),
	USER("ROLE_USER", "유저"),
	USER2("ROLE_USER2", "유저2"),
	USER3("ROLE_USER3", "유저3");

	private String roleKey;
	private String roleName;

	// 초기값은 검색이 가능하나 추가되는 Role은 찾을 수 없음.
//	public static String findByRoleKey(String roleName) {
//		for(RoleEnum role : RoleEnum.values()) {
//			if (role.roleName.equals(roleName)) {
//				return role.roleKey;
//			}
//		}
//		return null;
//	}

}