package com.project.vue.board;

import static org.junit.jupiter.api.DynamicTest.stream;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
	
	@GetMapping
	public ResponseEntity<Page<BoardEntity>> boardList(
			@RequestParam int pageIndex, 
			@RequestParam int pageSize,
			@RequestParam(required = false) String srchKey,
			@RequestParam(required = false) String srchVal) {
		return ResponseEntity.ok(boardService.findAll(pageIndex, pageSize, srchKey, srchVal));
	}
	
	@GetMapping("find/{id}")
	public ResponseEntity<BoardEntity> boardFindById(@PathVariable("id") Long id, Authentication auth) {
		return ResponseEntity.ok(boardService.findById(id, auth));
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
	        
	        ExcelService<BoardEntity> excelService = new ExcelService(dataList, BoardEntity.class);
	        
	        excelService.downloadExcel();
			
			return ResponseEntity.ok()
					 //attachement = 로컬에 저장, filename = 다운로드시 파일 이름 지정 
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + orgFileName +";")
					.header(HttpHeaders.CONTENT_TYPE, "ms-vnd/excel") 
					.body(new ByteArrayResource(stream.toByteArray()));
		} catch(Exception e) {
			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
		}
	}
	
//	@GetMapping("excel")
//	public ResponseEntity<ByteArrayResource> excel() {
//		try {
//			String sheetName = "게시판";
//	        List<String> headerList = Arrays.asList("No", "제목", "내용", "작성자", "조회수");   
//	        List<String> colList = Utils.getColList(BoardEntity.class);
//	        List<BoardEntity> dataList = boardService.findAll();
//	        
//	        ByteArrayOutputStream stream = excelDownload.buildExcelDocumentSXSSF(sheetName, headerList, colList, dataList);
// 
//        	String fileName = sheetName+"_"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))+".xlsx";
//			String orgFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
//			
//			return ResponseEntity.ok()
//					 //attachement = 로컬에 저장, filename = 다운로드시 파일 이름 지정 
//					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + orgFileName +";")
//					.header(HttpHeaders.CONTENT_TYPE, "ms-vnd/excel") 
//					.body(new ByteArrayResource(stream.toByteArray()));
//		} catch(Exception e) {
//			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
//		}
//	}
}
