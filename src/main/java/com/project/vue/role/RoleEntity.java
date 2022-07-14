package com.project.vue.role;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "ROLE")
@Data
public class RoleEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
    @Column(name = "role_key", unique = true)
    private String roleKey;

	@NotBlank
    @Column(name = "role_name")
    private String roleName;

}
