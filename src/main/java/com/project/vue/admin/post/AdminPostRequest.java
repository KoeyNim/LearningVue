package com.project.vue.admin.post;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminPostRequest {

	/** 조회 키 */
	private String srchKey;
	/** 조회 값 */
	private String srchVal;
}
