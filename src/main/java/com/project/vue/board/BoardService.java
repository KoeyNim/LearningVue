package com.project.vue.board;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vue.file.FileService;
import com.project.vue.file.image.ImageEntity;
import com.project.vue.file.image.ImageService;
import com.project.vue.specification.SearchSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	private final FileService fileService;
	private final ImageService imageService;
	
	private static final ObjectMapper OM = new ObjectMapper();

//	private final JPAQueryFactory queryFactory;

	// 수정시간이 바뀌게되는 이슈로 인해 querydsl로 세부 조작
	// 22.07.18 속도 개선 및 영속성 이슈로 인해 사용하지 않지만 참고 자료 용으로 주석 처리
//  @Transactional 
//	public void saveCount(Long id) {
//		QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
//		queryFactory.update(qBoardEntity)
//					.set(qBoardEntity.count, qBoardEntity.count.add(1))
//					.where(qBoardEntity.id.eq(id))
//					.execute();
//	}

	@Transactional // 자동으로 flush 명령을 수행하기 위해 어노테이션 사용
	public void updateCount(long boardSeqno) {
		boardRepository.updateCount(boardSeqno);
	}

	public void save(BoardSaveRequest req) {
		BoardEntity entity = new BoardEntity();

		entity.setTitle(req.getTitle());
		entity.setContent(req.getContent());
		entity.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());

		if(ObjectUtils.isNotEmpty(req.getFile())) { 
			entity.setFileEntity(fileService.upld(req.getFile()));
		}
		
		/* JSON String Deserialize **/
		if(ObjectUtils.isNotEmpty(req.getImage())) {
			try {
				JsonNode jn = OM.readTree(req.getImage());
				imageService.save(OM.treeToValue(jn, ImageEntity.class));
			} catch (JsonProcessingException | IllegalArgumentException e) {
				throw new RuntimeException(e); // TODO Exception..
			}
		}
		
		boardRepository.save(entity);
		
	}
	public void save(long boardSeqno, BoardSaveRequest req) {
		BoardEntity entity = boardRepository.findById(boardSeqno).orElseThrow(RuntimeException::new); // TODO Exception..
		String userid = SecurityContextHolder.getContext().getAuthentication().getName();

		if(!userid.equals(entity.getUserId())) {
			throw new RuntimeException("400 Error BadRequset"); // TODO Exception..
		}

		entity.setTitle(req.getTitle());
		entity.setContent(req.getContent());

		if(ObjectUtils.isNotEmpty(req.getFile())) {
			entity.setFileEntity(fileService.upld(req.getFile()));
		}

		/* JSON String Deserialize **/
		if(ObjectUtils.isNotEmpty(req.getImage())) {
			try {
				JsonNode jn = OM.readTree(req.getImage());
				imageService.save(OM.treeToValue(jn, ImageEntity.class));
			} catch (JsonProcessingException | IllegalArgumentException e) {
				throw new RuntimeException(e); // TODO Exception..
			}
		}

		boardRepository.save(entity);
	}

	public List<BoardEntity> findAll() {
		return boardRepository.findAll();
	}

	public Page<BoardEntity> findAll(Pageable page, BoardRequest srch) {
		return boardRepository.findAll(SearchSpecification.searchBoardSpecification(srch), page);
	}

	public BoardEntity findById(long boardSeqno) {
		BoardEntity boardEntity = boardRepository.findById(boardSeqno).orElseThrow();
		boardEntity.setAuthUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		return boardEntity;
	}

	public void deleteById(long boardSeqno) {
		BoardEntity boardEntity = boardRepository.findById(boardSeqno).orElseThrow();
		if (ObjectUtils.isNotEmpty(boardEntity.getFileEntity())) {
			File file = new File(boardEntity.getFileEntity().getFilePath() + boardEntity.getFileEntity().getFileNm());
//        	if (file.exists()) {
			file.delete();
//        	}
		}
		boardRepository.deleteById(boardSeqno);
	}
}