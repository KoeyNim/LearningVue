package com.project.vue.user.board;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import com.project.vue.common.PathConstants;
import com.project.vue.common.SimpleResponse;
import com.project.vue.common.excel.ExcelService;
import com.project.vue.common.exception.BizException;
import com.project.vue.common.exception.CustomExceptionHandler.ErrorCode;
import com.project.vue.user.payload.BoardRequest;
import com.project.vue.user.payload.BoardSaveRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX + "/" + PathConstants.BOARD)
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	/**
	 * 조건별 전체 조회
	 * @param page 페이지 데이터
	 * @param srch 검색 데이터
	 * @return Page<BoardEntity>
	 */
	@GetMapping
	public Page<BoardEntity> findAll(Pageable page, BoardRequest srch) {
		log.debug("api/v1/board - gets - page : {}, srch : {}", page, srch);
		return boardService.findAll(page, srch);
	}

	/**
	 * 상세 조회
	 * @param boardSeqno 키값
	 * @return BoardEntity
	 */
	@GetMapping(PathConstants.DETAIL)
	public BoardEntity findById(long boardSeqno) {
		log.debug("api/v1/detail/ - gets - boardSeqno : {}", boardSeqno);
		return boardService.findById(boardSeqno);
	}

	/**
	 * 등록
	 * @param req 등록 데이터
	 * @return ResponseEntity<SimpleResponse>
	 */
	@PostMapping(PathConstants.CREATE)
	public ResponseEntity<SimpleResponse> create(BoardSaveRequest req) {
		log.debug("api/v1/create/ - posts - req : {}", req);
		boardService.save(req);
		return ResponseEntity.ok(SimpleResponse.builder().message("게시글이 등록되었습니다.").build());
	}

	/**
	 * 수정
	 * @param boardSeqno 키값
	 * @param req 수정 데이터
	 * @return ResponseEntity<SimpleResponse>
	 */
	@PutMapping(PathConstants.UPDATE)
	public ResponseEntity<SimpleResponse> update(long boardSeqno, BoardSaveRequest req) {
		log.debug("api/v1/update/ - puts - boardSeqno : {}, req : {}", boardSeqno, req);
		boardService.save(boardSeqno, req);
		return ResponseEntity.ok(SimpleResponse.builder().message("게시글이 수정되었습니다.").build());
	}

	/**
	 * 삭제
	 * @param boardSeqno 키값
	 * @return ResponseEntity<SimpleResponse>
	 */
	@DeleteMapping(PathConstants.DELETE)
	public ResponseEntity<SimpleResponse> delete(long boardSeqno) {
		log.debug("api/v1/delete/ - delete - boardSeqno : {}", boardSeqno);
		boardService.deleteById(boardSeqno);
		return ResponseEntity.ok(SimpleResponse.builder().message("게시글이 삭제되었습니다.").build());
	}

	/**
	 * 엑셀 다운로드
	 * @return ResponseEntity<StreamingResponseBody>
	 */
	@GetMapping(PathConstants.EXCEL)
	public ResponseEntity<StreamingResponseBody> dwldExcel() {
		log.debug("api/v1/excel/ - gets - dwldExcel");
		try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
	        ExcelService<BoardEntity> excelService = new ExcelService<>(boardService.findAll(), BoardEntity.class);
	        String fileNm = excelService.create(bs);
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					 /** attachement = 로컬에 저장, filename = 파일 이름 */
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=" + URLEncoder.encode(fileNm, "UTF-8").replaceAll("\\+", "%20"))
					.body(os -> os.write(bs.toByteArray()));
		} catch(IOException ex) {
			throw new BizException("Excel Download Error", ex, ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
