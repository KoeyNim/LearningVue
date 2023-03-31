package com.project.vue.admin.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.vue.admin.payload.AdminBoardRequest;
import com.project.vue.common.PathConstants;
import com.project.vue.common.SimpleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/" + PathConstants.API_BOARD)
@RequiredArgsConstructor
public class AdminBoardController {

	private final AdminBoardService adminBoardService;

	/**
	 * 조건별 전체 조회
	 * @param page 페이지 데이터
	 * @param srch 검색 데이터
	 * @return Page<AdminBoardEntity>
	 */
	@GetMapping
	public ResponseEntity<Page<AdminBoardEntity>> findAll(Pageable page, AdminBoardRequest srch) {
		log.debug("api/board - get - page : {}, srch : {}", page, srch);
		return ResponseEntity.ok(adminBoardService.findAll(page, srch));
	}

	/**
	 * 상세 조회
	 * @param boardSeqno 키값
	 * @return AdminBoardEntity
	 */
	@GetMapping(PathConstants.API_DETAIL)
	public ResponseEntity<AdminBoardEntity> findById(long boardSeqno) {
		log.debug("api/detail - gets - boardSeqno : {}", boardSeqno);
		return ResponseEntity.ok(adminBoardService.findById(boardSeqno));
	}

	@PostMapping("create")
	public ResponseEntity<SimpleResponse> create(@RequestBody AdminBoardEntity post) {
		log.debug("create post : {}",post);
		adminBoardService.save(post);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 등록되었습니다.").build());
	}

	@PutMapping("update/{id}")
	public ResponseEntity<SimpleResponse> update(@RequestBody AdminBoardEntity post) {
		log.debug("update post : {}",post);
		adminBoardService.save(post);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 수정되었습니다.").build());
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<SimpleResponse> delete(@PathVariable("id") Long id) {
		log.debug("delete post id : {}",id);
		adminBoardService.deleteById(id);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 삭제되었습니다.").build());
	}
	
	@DeleteMapping("deletemany")
	public ResponseEntity<SimpleResponse> deleteMany(@RequestBody List<Long> ids) {
		log.debug("delete many post ids : {}",ids);
//		List<Long> longCasting = ids.stream().map(Long::parseLong).collect(Collectors.toList());
		adminBoardService.deleteAllByIdInBatch(ids);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 삭제되었습니다.").build());
	}

	//TODO ...
//	@GetMapping("excel")
//	public ResponseEntity<ByteArrayResource> excel() {
//		try {
//	        List<AdminPostEntity> dataList = adminPostService.findAll();
//	        
//	        ExcelService<AdminPostEntity> excelService = new ExcelService<>(dataList, AdminPostEntity.class);
//			
//			return excelService.create();
//		} catch(Exception e) {
//			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
//		}
//	}
}
