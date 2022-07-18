package com.project.vue.board;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.vue.common.Constants;
import com.project.vue.common.CookieCommon;
import com.project.vue.common.SimpleResponse;
import com.project.vue.common.excel.service.ExcelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX+"/board")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
	private final CookieCommon cookieCommon;
	
	@GetMapping
	public ResponseEntity<Page<BoardEntity>> boardList(
			@RequestParam int pageIndex, 
			@RequestParam int pageSize,
			@RequestParam(required = false) String srchKey,
			@RequestParam(required = false) String srchVal) {
		return ResponseEntity.ok(boardService.findAll(pageIndex, pageSize, srchKey, srchVal));
	}

	@GetMapping("find/{id}")
	public ResponseEntity<BoardEntity> boardFindById(@PathVariable("id") Long id, 
														HttpServletRequest request, HttpServletResponse response) {
		// 쿠키 유무 및 접근하는 게시글 조회 여부 확인
		if(cookieCommon.readCountCookie(response, request, id)) {
			// 조회수 반영
			boardService.updateCount(id);
		}
		return ResponseEntity.ok(boardService.findById(id));
	}

	@PostMapping("create")
	public ResponseEntity<SimpleResponse> boardCreate(@RequestBody BoardEntity board) {
		log.debug("create board : {}", board);
		boardService.save(board);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 등록되었습니다.").build());
	}

	@PutMapping("update/{id}")
	public ResponseEntity<SimpleResponse> boardUpdate(@RequestBody BoardEntity board) {
		log.debug("update board : {}", board);
		boardService.save(board);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 수정되었습니다.").build());
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<SimpleResponse> boardDelete(@PathVariable("id") Long id) {
		log.debug("delete board id : {}", id);
		boardService.deleteById(id);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 삭제되었습니다.").build());
	}
	
	@GetMapping("excel")
	public ResponseEntity<ByteArrayResource> excel() {
		try {
	        List<BoardEntity> dataList = boardService.findAll();
	        
	        ExcelService<BoardEntity> excelService = new ExcelService<>(dataList, BoardEntity.class);
	        
			return excelService.downloadExcel();
		} catch(Exception e) {
			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
		}
	}
}
