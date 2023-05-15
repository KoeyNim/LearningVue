package com.project.vue.user.member;

import java.util.ArrayList;
import java.util.Collection;

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

import org.springframework.security.core.GrantedAuthority;

import com.project.vue.common.StringCryptoConverter;
import com.project.vue.user.role.RoleEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @ToString
@Builder
@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(
		name = "MEMBER_SEQ_NO_01_GENERATOR",
		sequenceName = "MEMBER_SEQ_NO_01",
		initialValue = 1, allocationSize = 1
)
public class MemberEntity {

	@Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_NO_01_GENERATOR")
	private Long memberSeqno;

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

	/**
	 * 권한 가져오기 UserDetails implements 제거
	 * @return Collection<? extends GrantedAuthority>
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.getRoleKey();
            }
        });
        return collect;
	}
}
