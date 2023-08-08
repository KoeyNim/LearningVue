package com.project.vue.common.auth.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRepository extends JpaRepository<JwtEntity, JwtEntity.Key> {

}
