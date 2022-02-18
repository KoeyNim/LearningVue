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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;


	@Column(columnDefinition = "varchar(32)")
	private String userId;

	@Column(columnDefinition = "varchar(32)")
	private String userName;

	@Column(columnDefinition = "varchar(32)")
	private String userPwd;

	private Integer age;

	@Column(columnDefinition = "varchar(50)")
	private String email;

	@Column(columnDefinition = "varchar(10)")
	private String gender;

	@NotEmpty
	@NotBlank
	private Integer phone;

}
