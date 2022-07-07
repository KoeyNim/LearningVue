package com.project.vue.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchyRepository extends JpaRepository<RoleHierarchyEntity, Long> {
	
	boolean existsByRoleName(Role roleName);
}