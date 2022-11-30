package com.project.vue.board;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.project.vue.common.Constants;
import com.project.vue.common.CookieCommon;
import com.project.vue.common.SimpleResponse;
import com.project.vue.common.excel.service.ExcelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/board")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;
	private final CookieCommon cookieCommon;
	
	/**
	 * 게시글 조회
	 * @param page 페이징
	 * @param srch 검색 조건
	 * @return Page<BoardEntity>
	 */
	@GetMapping
	public Page<BoardEntity> findAll(Pageable page, BoardRequest srch) {
		log.debug("api/v1/board - gets - page : {}, srch : {}", page, srch);
		return boardService.findAll(page, srch);
	}

	/**
	 * 게시글 상세 조회
	 * @param boardSeqno 게시글 키 번호
	 * @return BoardEntity
	 */
	@GetMapping("detail")
	public BoardEntity findById(long boardSeqno) {
		log.debug("api/v1/detail/ - gets - boardSeqno : {}", boardSeqno);
		cookieCommon.readCountCookie(boardSeqno);
		return boardService.findById(boardSeqno);
	}

	@PostMapping("create")
	public ResponseEntity<SimpleResponse> create(BoardSaveRequest req) {
		log.debug("api/v1/create/ - posts - req : {}", req);
		boardService.save(req);
		return ResponseEntity.ok(SimpleResponse.builder().message("게시글이 등록되었습니다.").build());
	}

	@PutMapping("update")
	public ResponseEntity<SimpleResponse> update(long boardSeqno, BoardSaveRequest req) {
		log.debug("api/v1/update/ - puts - boardSeqno : {}, req : {}", boardSeqno, req);
		boardService.save(boardSeqno, req);
		return ResponseEntity.ok(SimpleResponse.builder().message("게시글이 수정되었습니다.").build());
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<SimpleResponse> delete(long boardSeqno) {
		log.debug("api/v1/delete/ - delete - boardSeqno : {}", boardSeqno);
		boardService.deleteById(boardSeqno);
		return ResponseEntity.ok(SimpleResponse.builder().message("게시글이 삭제되었습니다.").build());
	}
	
	@GetMapping("excel")
	public ResponseEntity<StreamingResponseBody> dwldExcel() {
		log.debug("api/v1/excel/ - gets - dwldExcel");
		try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
	        ExcelService<BoardEntity> excelService = new ExcelService<>(boardService.findAll(), BoardEntity.class);
	        String fileNm = excelService.create(bs);

			return ResponseEntity.ok()
					 //attachement = 로컬에 저장, filename = 다운로드시 파일 이름 지정
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20"))
					.body(os -> os.write(bs.toByteArray()));
		} catch(Exception e) {
			return new ResponseEntity<StreamingResponseBody>(HttpStatus.CONFLICT);
		}
	}
}
