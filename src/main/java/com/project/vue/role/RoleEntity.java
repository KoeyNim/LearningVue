package com.project.vue.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ROLE")
@Data
public class RoleEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING) // 상수가 아닌 String으로 저장
    private RoleEnum roleName;

}
