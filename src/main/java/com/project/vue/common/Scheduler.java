package com.project.vue.common;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.project.vue.common.file.QFileEntity;
import com.project.vue.user.board.QBoardEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

	private final JPAQueryFactory queryFactory;

	private final QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
	private final QFileEntity qFileEntity = QFileEntity.fileEntity;

	/**
	 * 사용하지 않는 파일 데이터 삭제 스케줄러
	 */
	@Transactional
	@Scheduled(cron = "0 0 10 * * ?")
	public void cleanGarbageFile() {
		List<Long> fileEntities = 
				queryFactory.select(qFileEntity.fileSeqno)
							.from(qFileEntity)
							.leftJoin(qBoardEntity).on(qBoardEntity.fileEntity().fileSeqno.eq(qFileEntity.fileSeqno))
							.where(qBoardEntity.fileEntity().fileSeqno.isNull())
							.fetch();

		if (CollectionUtils.isNotEmpty(fileEntities)) {
			long result = 
					queryFactory.delete(qFileEntity)
								.where(qFileEntity.fileSeqno.in(fileEntities))
								.execute();
			log.info("{} value Clean Garbage File", result);
		} else {
			log.info("Garbage file is not exists.");
		}
	}
}
