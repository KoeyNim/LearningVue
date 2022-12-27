package com.project.vue.board;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vue.file.FileService;
import com.project.vue.file.image.ImageService;
import com.project.vue.file.image.ImageTempResponse;
import com.project.vue.specification.SearchSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	private final FileService fileService;
	private final ImageService imageService;

	private static final ObjectMapper OM = new ObjectMapper();
	
	/** 수정시간이 바뀌게되는 이슈로 인해 querydsl로 세부 조작
	 *  22.07.18 느린 반영 및 영속성 이슈 발생으로 미사용 (참고용으로 남김)
	 */
//	private final JPAQueryFactory queryFactory;
//
//    @Transactional
//	public void saveCount(Long id) {
//		QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
//		queryFactory.update(qBoardEntity)
//					.set(qBoardEntity.count, qBoardEntity.count.add(1))
//					.where(qBoardEntity.boardSeqno.eq(id))
//					.execute();
//	}

	/**
	 * 조회수
	 * @param boardSeqno 게시글 키 번호
	 */
	@Transactional
	public void updateCount(long boardSeqno) {
		boardRepository.updateCount(boardSeqno);
	}

	/**
	 * 게시글 등록
	 * @param req BoardSaveRequest
	 */
	@Transactional
	public void save(BoardSaveRequest req) {
		BoardEntity entity = new BoardEntity();

		entity.setTitle(req.getTitle());
		entity.setContent(req.getContent());
		entity.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		
		/* 파일첨부 등록 **/
		if(ObjectUtils.isNotEmpty(req.getFile())) { 
			entity.setFileEntity(fileService.upld(req.getFile()));
		}
		boardRepository.save(entity);

		/* 에디터 이미지 등록 **/
		if(StringUtils.isNotBlank(req.getImgListJson())) {
			try {
				List<ImageTempResponse> deserializeList = Arrays.asList(OM.readValue(req.getImgListJson(), ImageTempResponse[].class));
				if(CollectionUtils.isNotEmpty(deserializeList)) {
					imageService.save(deserializeList, entity.getBoardSeqno());
				}
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e); // TODO Exception..
			}
		}
	}

	/**
	 * 게시글 수정
	 * @param boardSeqno 게시글 키 번호
	 * @param req BoardSaveRequest
	 */
	@Transactional
	public void save(long boardSeqno, BoardSaveRequest req) {
		BoardEntity entity = boardRepository.findById(boardSeqno).orElseThrow(RuntimeException::new); // TODO Exception..
		String userid = SecurityContextHolder.getContext().getAuthentication().getName();

		if(!userid.equals(entity.getUserId())) {
			throw new RuntimeException("400 Error BadRequset"); // TODO Exception..
		}

		entity.setTitle(req.getTitle());
		entity.setContent(req.getContent());
		
		/* 파일첨부 수정 **/
		if(ObjectUtils.isNotEmpty(req.getFile())) {
			entity.setFileEntity(fileService.upld(req.getFile()));
		}

		/* 에디터 이미지 수정 **/
		if(StringUtils.isNotBlank(req.getImgListJson())) {
			try {
				List<ImageTempResponse> deserializeList = Arrays.asList(OM.readValue(req.getImgListJson(), ImageTempResponse[].class));
				if(CollectionUtils.isNotEmpty(deserializeList)) {
					imageService.save(deserializeList, boardSeqno);
				}
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e); // TODO Exception..
			}
		}
		boardRepository.save(entity);
	}

	/**
	 * 게시글 조회
	 * @return List<BoardEntity>
	 */
	public List<BoardEntity> findAll() {
		return boardRepository.findAll();
	}

	/**
	 * 게시글 조회
	 * @param page 페이징
	 * @param srch 검색 조건
	 * @return Page<BoardEntity>
	 */
	public Page<BoardEntity> findAll(Pageable page, BoardRequest srch) {
		return boardRepository.findAll(SearchSpecification.searchBoardSpecification(srch), page);
	}

	/**
	 * 게시글 상세 조회
	 * @param boardSeqno 게시글 키 번호
	 * @return BoardEntity
	 */
	public BoardEntity findById(long boardSeqno) {
		BoardEntity boardEntity = boardRepository.findById(boardSeqno).orElseThrow();
		boardEntity.setAuthUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		return boardEntity;
	}

	/**
	 * 게시글 삭제
	 * @param boardSeqno 게시글 키 번호
	 */
	@Transactional
	public void deleteById(long boardSeqno) {
		BoardEntity boardEntity = boardRepository.findById(boardSeqno).orElseThrow(RuntimeException::new); // TODO Exception..
		if(ObjectUtils.isNotEmpty(boardEntity.getFileEntity())) {
			fileService.delete(boardEntity.getFileEntity());
		}
		imageService.deleteAll(boardSeqno);
		boardRepository.deleteById(boardSeqno);
	}
}