package com.project.vue.admin.post;

import java.util.List;

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

import com.project.vue.common.SimpleResponse;
import com.project.vue.common.excel.service.ExcelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class AdminPostController {
	
	private final AdminPostService adminPostService;
	
	@GetMapping
	public ResponseEntity<Page<AdminPostEntity>> postList(
			@RequestParam int pageIndex, 
			@RequestParam int pageSize,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String srchKey,
			@RequestParam(required = false) String srchVal) {
		return ResponseEntity.ok(adminPostService.findAll(pageIndex, pageSize, sortKey, order, srchKey, srchVal));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<AdminPostEntity> postFindById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(adminPostService.findById(id));
	}

	@PostMapping("create")
	public ResponseEntity<SimpleResponse> postCreate(@RequestBody AdminPostEntity post) {
		log.debug("create post : {}",post);
		adminPostService.save(post);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 등록되었습니다.").build());
	}

	@PutMapping("update/{id}")
	public ResponseEntity<SimpleResponse> postUpdate(@RequestBody AdminPostEntity post) {
		log.debug("update post : {}",post);
		adminPostService.save(post);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 수정되었습니다.").build());
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<SimpleResponse> postDelete(@PathVariable("id") Long id) {
		log.debug("delete post id : {}",id);
		adminPostService.deleteById(id);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 삭제되었습니다.").build());
	}
	
	@DeleteMapping("deletemany")
	public ResponseEntity<SimpleResponse> postDeleteMany(@RequestBody List<Long> ids) {
		log.debug("delete many post ids : {}",ids);
//		List<Long> longCasting = ids.stream().map(Long::parseLong).collect(Collectors.toList());
		adminPostService.deleteAllByIdInBatch(ids);
		return ResponseEntity.ok(SimpleResponse.builder().message("글이 삭제되었습니다.").build());
	}
	
	@GetMapping("excel")
	public ResponseEntity<ByteArrayResource> excel() {
		try {
	        List<AdminPostEntity> dataList = adminPostService.findAll();
	        
	        ExcelService<AdminPostEntity> excelService = new ExcelService<>(dataList, AdminPostEntity.class);
			
			return excelService.downloadExcel();
		} catch(Exception e) {
			return new ResponseEntity<ByteArrayResource>(HttpStatus.CONFLICT);
		}
	}
}
