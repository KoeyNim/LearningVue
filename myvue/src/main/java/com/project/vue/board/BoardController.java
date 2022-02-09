package com.project.vue.board;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.vue.common.SimpleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
	@GetMapping("")
	public String BoardList() {
		
		return "board/board";
	}
	
	@GetMapping("/detail/{boardId}")
	public String BoardDetail(@PathVariable("boardId") Long boardId) {
		
		return "board/board-detail";
	}
	
	@GetMapping("/regist")
	public String BoardRegist() {
		
		return "board/board-form";
	}
	
	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<SimpleResponse> BoardCreate(BoardEntity board) {
		log.debug("!@#!@#{}" ,board);
		return ResponseEntity.ok(SimpleResponse.builder().success(boardService.save(board)).build());
	}
	
	@PutMapping("/modify/{boardId}")
	public ResponseEntity BoardModify(@PathVariable("boardId") Long boardId) {
		return null;
	}
	
	@DeleteMapping("/delete/{boardId}")
	public ResponseEntity BoardDelete(@PathVariable("boardId") Long boardId) {
		return null;
	}
}
