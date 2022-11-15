package com.project.vue.board;

import java.io.File;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.vue.file.FileRepository;
import com.project.vue.specification.SearchSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	private final FileRepository fileRepository;

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
	public void updateCount(Long id) {
		boardRepository.updateCount(id);
	}

	public void save(BoardEntity board) {
		if (ObjectUtils.isEmpty(board.getId())) { // board.id 값 체크 (없으면 등록상황이다.)
			board.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
			boardRepository.save(board);
			return;
		}

		BoardEntity boardEntity = boardRepository.findById(board.getId()).orElseThrow(); // 수정 전에 저장된 board 객체를 찾는다.

		if (ObjectUtils.isNotEmpty(boardEntity.getFileEntity()) // 수정전 board 객체의 파일이 비어있는지 확인
				&& ObjectUtils.notEqual(board.getFileEntity().getId(), // 저장할 파일과 저장 되어있는 파일의 id값 일치여부 확인
						boardEntity.getFileEntity().getId())) {
			fileRepository.deleteById(boardEntity.getFileEntity().getId()); // 조건에 모두 만족하는 파일 데이터를 삭제 (수정되어 필요없는 파일)
		}
		boardRepository.save(board);
	}

	public List<BoardEntity> findAll() {
		return boardRepository.findAll();
	}

	public Page<BoardEntity> findAll(Pageable page, BoardRequest srch) {
		return boardRepository.findAll(SearchSpecification.searchBoardSpecification(srch), page);
	}

	public BoardEntity findById(Long id) {
		return boardRepository.findById(id).orElseThrow();
	}

	public BoardEntity findById(Long id, Authentication auth) {
		BoardEntity boardEntity = findById(id);
		boardEntity.setAuthUserId(auth.getName());
		return boardEntity;
	}

	public void deleteById(Long id) {
		BoardEntity boardEntity = findById(id);
		if (ObjectUtils.isNotEmpty(boardEntity.getFileEntity())) {
			File file = new File(boardEntity.getFileEntity().getFilePath() + boardEntity.getFileEntity().getFileNm());
//        	if (file.exists()) {
			file.delete();
//        	}
		}
		boardRepository.deleteById(id);
	}
}