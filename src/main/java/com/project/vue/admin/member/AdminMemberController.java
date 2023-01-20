package com.project.vue.admin.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/users")
@RequiredArgsConstructor
public class AdminMemberController {

	private final AdminMemberService adminmemberService;

	@GetMapping
	public ResponseEntity<Page<AdminMemberEntity>> findAll(Pageable page, AdminMemberRequest srch) {
		log.debug("api/users - get - page : {}, srch : {}", page, srch);
		return ResponseEntity.ok(adminmemberService.findAll(page, srch));
	}
}
