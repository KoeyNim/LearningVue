package com.project.vue.admin.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminBoardRequest {

	/** 조회 키 */
	private String srchKey;
	/** 조회 값 */
	private String srchVal;
}
