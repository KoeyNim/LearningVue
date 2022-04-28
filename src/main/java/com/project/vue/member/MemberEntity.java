package com.project.vue.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@NotNull
	@Column(columnDefinition = "varchar(16)")
	private String userId;
	
	@NotNull
	@Column(columnDefinition = "varchar(8)")
	private String userName;
	
	@NotNull
	@Column(columnDefinition = "varchar(255)")
	private String userPwd;
	
	@NotNull
	private Integer age;
	
	@NotNull
	@Column(columnDefinition = "varchar(50)")
	private String email;
	
	@NotNull
	@Column(columnDefinition = "varchar(4)")
	private String gender;
	
	@NotNull
	@Column(columnDefinition = "varchar(11)")
	private String phone;

}
