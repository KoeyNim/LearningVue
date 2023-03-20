package com.project.vue.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
public class QueryDSLConfig {
	
	/** EntityManager를 빈으로 주입할 때 사용 (싱글톤 기반인 스프링에서 동시성 문제 해결) */
	@PersistenceContext
    private EntityManager em;

    @Bean
    JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
