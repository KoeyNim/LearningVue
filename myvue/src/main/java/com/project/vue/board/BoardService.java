package com.project.vue.board;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.vue.file.FileEntity;
import com.project.vue.file.FileRepository;
import com.project.vue.file.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final FileRepository fileRepository;
	
	private final FileService fileService;

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
    public void save(BoardEntity board, MultipartFile imgFile) throws Exception {
		log.debug("## board: {}", board);
		log.debug("## imgFile: {}", imgFile);
		if (imgFile != null) {
			FileEntity file = fileService.save(imgFile);
			board.setFileEntity(file);
			log.debug("## fileEntity: {}", board.getFileEntity());
		}
		boardRepository.save(board);
    }
	
    public Page<BoardEntity> findAll(int pageIndex, int pageSize) {
    	PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("registDate").descending());
		return boardRepository.findAll(pageRequest);
    }
    
    public Optional<BoardEntity> findById(Long id) {
		return boardRepository.findById(id);
    }
    
    public void deleteById(Long id) {
    	log.debug("## id: {}", id);
    	boardRepository.deleteById(id);
    }
	
}