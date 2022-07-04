package com.project.vue.Role;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleHierarchyService {
	
	private final RoleHierarchyRepository roleHierarchyRepository;
	
	@Transactional
	public String findAllHierarchy() {
		// db 초기값 생성 aplication.yml 에 sql 자동실행 하기 후 Bean에 해당 method 적용
        List<RoleHierarchyEntity> roleHierarchies = roleHierarchyRepository.findAll();

        Iterator<RoleHierarchyEntity> iterator = roleHierarchies.iterator();
        StringBuilder concatRoles = new StringBuilder();
        while (iterator.hasNext()) {
        	RoleHierarchyEntity roleHierarchy = iterator.next();
            if (ObjectUtils.isNotEmpty(roleHierarchy.getParentName())) {
                concatRoles.append(roleHierarchy.getParentName().getChildName());
                concatRoles.append(" > ");
                concatRoles.append(roleHierarchy.getChildName());
                concatRoles.append("\n");
            }
        }
        return concatRoles.toString();
	}

}
