package com.project.vue.user.board;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.vue.common.CookieCommon;
import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.ErrorCode;
import com.project.vue.common.file.FileService;
import com.project.vue.common.image.ImageService;
import com.project.vue.user.UserSearchSpecification;
import com.project.vue.user.payload.BoardRequest;
import com.project.vue.user.payload.BoardSaveRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final CookieCommon cookieCommon;
	private final FileService fileService;
	private final ImageService imageService;

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
	 * 조회
	 * @return List<BoardEntity>
	 */
	public List<BoardEntity> findAll() {
		return boardRepository.findAll();
	}

	/**
	 * 조회
	 * @param page 페이지 쿼리
	 * @param srch 검색 쿼리
	 * @return Page<BoardEntity>
	 */
	public Page<BoardEntity> findAll(Pageable page, BoardRequest srch) {
		return boardRepository.findAll(UserSearchSpecification.searchBoardSpecification(srch), page);
	}

	/**
	 * 게시글 상세 조회
	 * @param boardSeqno 키값
	 * @return BoardEntity
	 */
	@Transactional
	public BoardEntity findById(long boardSeqno) {
		BoardEntity entity = boardRepository.findById(boardSeqno)
				.orElseThrow(() -> new BizException("Data is Not Found", ErrorCode.NOT_FOUND));
		entity.setAuthUserId(SecurityContextHolder.getContext().getAuthentication().getName());
		if (cookieCommon.isReadCountCookie(boardSeqno)) boardRepository.updateCount(boardSeqno);
		return entity;
	}

	/**
	 * 등록
	 * @param req 등록 데이터
	 */
	@Transactional
	public void save(BoardSaveRequest req) {
		BoardEntity entity = new BoardEntity();

		entity.setTitle(req.getTitle());
		entity.setContent(req.getContent());
		entity.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());

		/** 첨부파일 저장 */
		if(ObjectUtils.isNotEmpty(req.getFile())) entity.setFileEntity(fileService.upload(req.getFile()));

		boardRepository.save(entity);

		/** 에디터 이미지 저장 */
		imageService.save(req.getImgListJson(), entity.getBoardSeqno());
	}

	/**
	 * 수정
	 * @param boardSeqno 키값
	 * @param req 수정 데이터
	 */
	@Transactional
	public void save(long boardSeqno, BoardSaveRequest req) {
		BoardEntity entity = boardRepository.findById(boardSeqno)
				.orElseThrow(() -> new BizException("Data is Not Found", ErrorCode.NOT_FOUND));
		String userid = SecurityContextHolder.getContext().getAuthentication().getName();

		if(!StringUtils.equals(userid, entity.getUserId())) throw new BizException("Userid is Not Equals", ErrorCode.BAD_REQUEST);

		entity.setTitle(req.getTitle());
		entity.setContent(req.getContent());

		/** 첨부파일 저장 */
		if(ObjectUtils.isNotEmpty(req.getFile())) entity.setFileEntity(fileService.upload(req.getFile()));

		boardRepository.save(entity);

		/** 에디터 이미지 저장 */
		imageService.save(req.getImgListJson(), entity.getBoardSeqno());
	}

	/**
	 * 삭제
	 * @param boardSeqno 키값
	 */
	@Transactional
	public void deleteById(long boardSeqno) {
		BoardEntity entity = boardRepository.findById(boardSeqno)
				.orElseThrow(() -> new BizException("Data is Not Found", ErrorCode.NOT_FOUND));

		if(ObjectUtils.isNotEmpty(entity.getFileEntity())) fileService.delete(entity.getFileEntity());
		imageService.deleteAll(entity.getBoardSeqno());
		boardRepository.deleteById(entity.getBoardSeqno());
	}
}