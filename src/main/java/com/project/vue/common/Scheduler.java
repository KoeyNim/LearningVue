package com.project.vue.common;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.project.vue.board.QBoardEntity;
import com.project.vue.file.QFileEntity;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {
	
	private final JPAQueryFactory queryFactory;
	
	@Transactional
	@Scheduled(cron = "0 0 10 * * ?")
	public void cleanGarbageFile() {
		QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
		QFileEntity qFileEntity = QFileEntity.fileEntity;
		
		Boolean garbageExists = ObjectUtils.isNotEmpty(
						queryFactory
						.select(qFileEntity.id)
						.from(qFileEntity)
						.where(JPAExpressions.selectFrom(qBoardEntity)
						.where(qFileEntity.id.eq(qBoardEntity.fileEntity().id)).notExists())
						.fetchOne());
		
		if (garbageExists) {
			long result = queryFactory.delete(qFileEntity)
			.where(JPAExpressions.selectFrom(qBoardEntity)
			.where(qFileEntity.id.eq(qBoardEntity.fileEntity().id))
			.notExists()).execute();
			log.info("{} value CleanGarbageFile", result);
		} else {
			log.info("Garbage file is not exists.");
		}
	}
}
