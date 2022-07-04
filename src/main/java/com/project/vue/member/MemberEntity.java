package com.project.vue.member;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.vue.Role.Role;
import com.project.vue.common.StringCryptoConverter;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "member")
public class MemberEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

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
	
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.toString();
            }
        });
        return collect;
	}

	@Override
	public String getPassword() {
		return this.userPwd;
	}

	@Override
	public String getUsername() {
		return this.userId;
	}

    /**
     * 계정 만료 여부
     *  true : 만료안됨
     *  false : 만료됨
     */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

    /**
     * 계정 잠김 여부
     *  true : 잠기지 않음
     *  false : 잠김
     */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

    /**
     * 계정 비밀번호 만료 여부
     *  true : 만료 안됨
     *  false : 만료됨
     */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	
    /**
     * 계정 활성화 여부
     *  true : 활성화됨
     *  false : 활성화 안됨
     */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
