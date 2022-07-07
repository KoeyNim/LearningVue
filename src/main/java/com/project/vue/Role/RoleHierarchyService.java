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
	
	@Transactional @PostConstruct // 의존성(Bean)이 초기화 된 직후에 한번만 실행 (서버 시작시 한번만 실행)
	public void initRoleData() {
		List<RoleHierarchyEntity> list = new ArrayList<>();
		
		for (Role role : Role.values()) {
			// 중복 데이터 확인 (select를 너무 자주사용함, 보완 가능하면 수정)
			if (!roleHierarchyRepository.existsByRoleName(role)) {
				RoleHierarchyEntity roleHierarchy = new RoleHierarchyEntity();
				// ADMIN 권한 체크 후 list에 추가
				if (role.equals(Role.ROLE_ADMIN)) {
					roleHierarchy.setRoleName(role);
					list.add(roleHierarchy);
					log.debug("roleHierarchy_admin : {}", roleHierarchy);
					continue;
				}
				roleHierarchy.setRoleName(role);
				// 권한 추가시 중복 값 체크 후 중복된 데이터는 list에서 빠지므로 사용할 수 없는 코드임. 에러 발생
				roleHierarchy.setParent(list.get(role.ordinal()-1));
				list.add(roleHierarchy);
				log.debug("roleHierarchy_another : {}", roleHierarchy);
			}
		}
		roleHierarchyRepository.saveAll(list);
	}

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
