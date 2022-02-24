package com.project.vue.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;
/*
 * Querydsl을 사용하기 위해 선언
 */
@Configuration
public class QuerydslConfiguration {
    
	@PersistenceContext
    private EntityManager em;
	
	@Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
