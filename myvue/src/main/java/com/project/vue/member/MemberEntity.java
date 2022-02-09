package com.project.vue.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class MemberEntity {

	@Id
	@NotEmpty
	@NotBlank
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "varchar(32)")
	private String userId;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "varchar(32)")
	private String userName;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "varchar(32)")
	private String userPwd;

	@NotEmpty
	@NotBlank
	private Integer age;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "varchar(50)")
	private String email;

	@NotEmpty
	@NotBlank
	@Column(columnDefinition = "varchar(10)")
	private String gender;

	@NotEmpty
	@NotBlank
	private Integer phone;

}
