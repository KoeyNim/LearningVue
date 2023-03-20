package com.project.vue.admin.member;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.vue.common.StringCryptoConverter;
import com.project.vue.user.role.RoleEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "member")
@SequenceGenerator(
		name = "MEMBER_SEQ_NO_01_GENERATOR",
		sequenceName = "MEMBER_SEQ_NO_01",
		initialValue = 1, allocationSize = 1
)
public class AdminMemberEntity {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_NO_01_GENERATOR")
	@Column(name = "memberSeqno")
	private Long id;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String userId;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
    @JsonIgnore
	private String userPwd;

	@NotBlank
	@Column(columnDefinition = "varchar(255)")
	@Convert(converter = StringCryptoConverter.class)
	private String userName;

	@NotBlank
	@Column(columnDefinition = "varchar(4)")
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
