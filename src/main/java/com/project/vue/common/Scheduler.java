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
	
	/**
	 * 사용하지 않는 파일 데이터 삭제 스케줄러
	 */
	@Transactional
	@Scheduled(cron = "0 0 10 * * ?")
	public void cleanGarbageFile() {
		QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
		QFileEntity qFileEntity = QFileEntity.fileEntity;
		
		Boolean garbageExists = ObjectUtils.isNotEmpty(
						queryFactory
						.select(qFileEntity.fileSeqno)
						.from(qFileEntity)
						.where(JPAExpressions.selectFrom(qBoardEntity)
						.where(qFileEntity.fileSeqno.eq(qBoardEntity.fileEntity().fileSeqno)).notExists())
						.fetch());

		if (garbageExists) {
			long result = queryFactory.delete(qFileEntity)
			.where(JPAExpressions.selectFrom(qBoardEntity)
			.where(qFileEntity.fileSeqno.eq(qBoardEntity.fileEntity().fileSeqno))
			.notExists()).execute();
			log.info("{} value CleanGarbageFile", result);
		} else {
			log.info("Garbage file is not exists.");
		}
	}
}
