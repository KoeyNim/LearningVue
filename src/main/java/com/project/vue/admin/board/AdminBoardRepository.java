package com.project.vue.admin.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminBoardRepository extends JpaRepository<AdminBoardEntity, Long>, JpaSpecificationExecutor<AdminBoardEntity> {

}