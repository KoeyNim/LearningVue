package com.project.vue.board;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	@Transactional
    public boolean save(BoardEntity board) {
    		
    	return this.boardRepository.save(board) != null;
    		
    }
}

