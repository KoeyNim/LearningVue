package com.project.vue.Role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleHierarchyService {
	
	private final RoleHierarchyRepository roleHierarchyRepository;
	
	@Transactional @PostConstruct // 의존성(Bean)이 초기화 된 직후에 한번만 실행
	public void initRoleData() {
		RoleHierarchyEntity roleHierarchy = new RoleHierarchyEntity();
		List<RoleHierarchyEntity> list = new ArrayList<>();
		
		roleHierarchyRepository.saveAll(list);
	}
	
	@Transactional
	public String findAllHierarchy() {
        List<RoleHierarchyEntity> roleHierarchies = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchyEntity> iterator = roleHierarchies.iterator();
        StringBuilder concatRoles = new StringBuilder();
        while (iterator.hasNext()) {
        	RoleHierarchyEntity roleHierarchy = iterator.next();
            if (ObjectUtils.isNotEmpty(roleHierarchy.getParent())) {
                concatRoles.append(roleHierarchy.getParent().getRoleName());
                concatRoles.append(" > ");
                concatRoles.append(roleHierarchy.getRoleName());
                concatRoles.append("\n");
            }
        }
        log.debug("roleHierarchy = {}", concatRoles.toString());
        return concatRoles.toString();
	}

}
