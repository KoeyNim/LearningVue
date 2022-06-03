package com.project.vue.admin.member;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AdminMemberController {
	
	private final AdminMemberService adminmemberService;
	
	@GetMapping
	public ResponseEntity<Page<AdminMemberEntity>> postList(
			@RequestParam int pageIndex, 
			@RequestParam int pageSize,
			@RequestParam(required = false) String sortKey,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String srchKey,
			@RequestParam(required = false) String srchVal) {
		log.debug("@@ {} {} {} {}",pageIndex, pageSize, sortKey, order);
		return ResponseEntity.ok(adminmemberService.findAll(pageIndex, pageSize, sortKey, order, srchKey, srchVal));
	}
	
//	@ResponseBody
//	@PostMapping(Constants.REQUEST_MAPPING_PREFIX+"/member/signup")
//	public ResponseEntity<SimpleResponse> signUp(@RequestBody AdminMemberEntity member) {
//		adminmemberService.save(member);
//		return ResponseEntity.ok(SimpleResponse.builder().message("회원가입이 완료되었습니다.").build());
//	}
}
