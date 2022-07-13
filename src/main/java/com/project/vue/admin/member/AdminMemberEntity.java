package com.project.vue.admin.member;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.project.vue.common.StringCryptoConverter;
import com.project.vue.role.RoleEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class AdminMemberEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memberId")
	private Long id;

	@NotNull
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String userId;
	
	@NotNull
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String userName;
	
	@NotNull
	@Column(columnDefinition = "varchar(255)")
	private String userPwd;
	
	@NotNull
	private Integer age;
	
	@NotNull
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String email;
	
	@NotNull
	@Column(columnDefinition = "varchar(4)")
	private String gender;
	
	@NotNull
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String phone;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="role", referencedColumnName = "role_Key")
	private RoleEntity role;

}
