package com.project.vue.role;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "role")
@Data
public class RoleEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	/** 기본키 */
	private Long id;

	@NotNull
    @Column(name = "role_key")
	/** role 키 */
    private String roleKey;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent", referencedColumnName = "role_key")
    @JsonIgnore
    /** 권한계층 부모 role 키 */
    private RoleEntity parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL})
    @JsonIgnore
    /** 권한계층 */
    private Set<RoleEntity> roleHierarchy = new HashSet<RoleEntity>();

}
