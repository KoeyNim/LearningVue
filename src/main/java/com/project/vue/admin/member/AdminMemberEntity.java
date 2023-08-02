package com.project.vue.admin.member;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.project.vue.common.converter.GenderConverter;
import com.project.vue.common.converter.StringCryptoConverter;
import com.project.vue.user.role.RoleEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "member")
public class AdminMemberEntity {

	@Id
	@Column(name = "memberUid", columnDefinition = "varchar(32)")
	private String id;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String userId;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	private String userPwd;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String userName;

	@NotBlank
	@Column(columnDefinition = "varchar(1)")
	@Convert(converter = GenderConverter.class)
	private String gender;

	@NotNull
	private Integer age;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String email;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String phone;

	@NotNull
	@OneToOne
	@JoinColumn(name="role", referencedColumnName = "role_Key")
	private RoleEntity role;

}
