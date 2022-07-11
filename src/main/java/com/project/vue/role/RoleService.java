package com.project.vue.role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.project.vue.role.rolehierarchy.RoleHierarchyEntity;
import com.project.vue.role.rolehierarchy.RoleHierarchyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
	
	private final RoleRepository roleRepository;
	private final RoleHierarchyRepository roleHierarchyRepository;
	
	@Transactional @PostConstruct // @PostConstruct : 의존성(Bean)이 초기화 된 직후에 한번만 실행 (서버 시작시 한번만 실행)
	public void initRoleData() {

		// 권한 초기 데이터 삽입
		if(roleRepository.count() == 0) {
			List<RoleEntity> roleLists = new ArrayList<>();
			for (RoleEnum roleEnum : RoleEnum.values()) {
				RoleEntity roleEntity = new RoleEntity();
				roleEntity.setRoleName(roleEnum);
				roleLists.add(roleEntity);
			}
			roleRepository.saveAll(roleLists);
		}

		// 권한 계층 초기 데이터 삽입
		if(roleHierarchyRepository.count() == 0) {
			List<RoleHierarchyEntity> list = new ArrayList<>();
			List<RoleEntity> roleLists = roleRepository.findAll();
			for (RoleEntity role : roleLists) {
				RoleHierarchyEntity roleHierarchy = new RoleHierarchyEntity();
				// ADMIN 권한 체크 후 list에 추가
				if (role.getRoleName().equals(RoleEnum.ROLE_ADMIN)) {
					roleHierarchy.setRoleName(role.getRoleName());
					list.add(roleHierarchy);
					continue;
				}
				roleHierarchy.setRoleName(role.getRoleName());
				roleHierarchy.setParent(list.get(role.getRoleName().ordinal()-1));
				list.add(roleHierarchy);
			}
			roleHierarchyRepository.saveAll(list);
		}
	}

	// 계층권한 String Build
	public String BuildAllHierarchy() {
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
