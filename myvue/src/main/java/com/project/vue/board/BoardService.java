package com.project.vue.board;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

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

	@Transactional
    public void save(BoardEntity board) {
		log.debug("## board: {}", board);
		boardRepository.save(board);
    }
	
    public List<BoardEntity> findAll() {
		return boardRepository.findAll();
    }
    
    public Optional<BoardEntity> findById(Long id) {
		return boardRepository.findById(id);
    }
	
}