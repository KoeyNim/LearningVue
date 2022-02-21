package com.project.vue.board;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.vue.specification.SearchSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;

	/*
    public boolean save(BoardEntity board) {
		try {
			log.debug("## board: {}", board);
			boardRepository.save(board);
			return true;
		} catch (ConstraintViolationException e) {
			log.debug("## 1");
			return false;
		} catch (Exception e) {
			log.debug("## 2");
			return false;
		}
    	//return this.boardRepository.save(board) != null; // 변경해야됨
    }*/

//	@Transactional
//    public void save(BoardEntity board, MultipartFile imgFile) throws Exception {
//		log.debug("## board: {}", board);
//		log.debug("## imgFile: {}", imgFile);
//		if (imgFile != null) {
//			FileEntity file = fileService.save(imgFile);
//			board.setFileEntity(file);
//			log.debug("## fileEntity: {}", board.getFileEntity());
//		}
//		boardRepository.save(board);
//    }
	
	@Transactional
    public void save(BoardEntity board) {
		boardRepository.save(board);
    }
	
    public Page<BoardEntity> findAll(int pageIndex, int pageSize, String srchKey, String srchVal) {
    	PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("registDate").descending());
    	BoardEntity boardEntity = new BoardEntity();
    	boardEntity.setSrchKey(srchKey);
    	boardEntity.setSrchVal(srchVal);
		return boardRepository.findAll(SearchSpecification.searchBoardSpecification(boardEntity), pageRequest);
    }
    
    public Optional<BoardEntity> findById(Long id) {
		return boardRepository.findById(id);
    }
    
    public void deleteById(Long id) {
    	log.debug("## id: {}", id);
    	boardRepository.deleteById(id);
    }
	
}