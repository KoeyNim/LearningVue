package com.project.vue.user.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberSignUpRequest {

	/** 아이디 */
	private String userId;
	/** 비밀번호 */
	private String userPwd;
	/** 이름 */
	private String userName;
	/** 성별 */
	private String gender;
	/** 나이 */
	private Integer age;
	/** 이메일 */
	private String email;
	/** 휴대폰번호 */
	private String phone;
}
