package com.project.vue.board;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.vue.common.Constants;
import com.project.vue.common.ExcelDownload;
import com.project.vue.common.SimpleResponse;
import com.project.vue.common.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ControllerAdvice
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX+"/board")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	
	private final ExcelDownload excelDownload;
	
	@ResponseBody
	@GetMapping
	public ResponseEntity<Page<BoardEntity>> boardList(
			@RequestParam int pageIndex, 
			@RequestParam int pageSize,
			@RequestParam(required = false) String srchKey,
			@RequestParam(required = false) String srchVal) {
		return ResponseEntity.ok(boardService.findAll(pageIndex, pageSize, srchKey, srchVal));
	}
	
	@GetMapping("find/{id}")
	public ResponseEntity<Map<String, Object>> boardFindById(@PathVariable("id") Long id, Authentication authentication) {
		BoardEntity boardEntity = null;
		Map<String, Object> result = new HashMap<>();
		boolean r = true;
		try {
			boardEntity = boardService.findById(id);
			result.put("data", boardEntity);
			result.put("authUserId", authentication.getPrincipal());
		} catch (Exception e) {
			e.printStackTrace();
			r = false;
		}
		return r ? ResponseEntity.ok().body(result) : ResponseEntity.notFound().build();
	}

	@ResponseBody
	@PostMapping("create")
	public ResponseEntity<SimpleResponse> boardCreate(@RequestBody BoardEntity board) {
		log.debug("##board {}",board);
		boolean r = true;
		try {
			boardService.save(board);;
		} catch (Exception e) {
			e.printStackTrace();
			r = false;
		}
		return ResponseEntity.ok(SimpleResponse.builder()
					.success(r)
					.message(r ? "글이 등록되었습니다." : "등록 에러.")
					.build());
	}

	@PutMapping("update/{id}")
	public ResponseEntity<SimpleResponse> boardupdate(@RequestBody BoardEntity board, @PathVariable("id") Long id) {
		boolean r = true;
		try {
			boardService.save(board);
		} catch (Exception e) {
			e.printStackTrace();
			r = false;
		}
		return ResponseEntity.ok(SimpleResponse.builder()
					.success(r)
					.message(r ? "글이 수정되었습니다." : "수정 에러.")
					.build());
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<SimpleResponse> boardDelete(@PathVariable("id") Long id) {
		boolean r = true;
		try {
			boardService.deleteById(id);
		} catch (Exception e) {
			r = false;
		}
		return ResponseEntity.ok(SimpleResponse.builder()
					.success(r)
					.message(r ? "글이 삭제되었습니다." : "삭제 에러.")
					.build());
	}
	
	@GetMapping("excel")
	public ResponseEntity<ByteArrayResource> excel() {
		try {
			String sheetName = "게시판";
	        List<String> headerList = Arrays.asList("No", "제목", "내용", "작성자", "조회수", "FileNo");   
	        List<String> colList = Utils.getColList(BoardEntity.class);
	        List<BoardEntity> dataList = boardService.findAll();
	        
	        ByteArrayOutputStream stream = excelDownload.buildExcelDocumentSXSSF(headerList, colList, dataList, sheetName);
 
        	String fileName = sheetName+"_"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))+".xlsx";
			String orgFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			
			return ResponseEntity.ok()
					 //attachement = 로컬에 저장, filename = 다운로드시 파일 이름 지정 
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + orgFileName +";")
					.header(HttpHeaders.CONTENT_TYPE, "ms-vnd/excel") 
					.body(new ByteArrayResource(stream.toByteArray()));
		} catch(Exception e) {
			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
		}
	}
	
	/*
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<SimpleResponse> exception2() {
		return ResponseEntity
					.badRequest()
					.body(SimpleResponse.builder()
					.success(false)
					.message("잘못된 요청입니다. 222")
					.build());
	}
	*/
}
