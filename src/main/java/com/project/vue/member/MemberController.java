package com.project.vue.member;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.vue.common.Constants;
import com.project.vue.common.SimpleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@ResponseBody
	@PostMapping(Constants.REQUEST_MAPPING_PREFIX+"/member/signup")
	public ResponseEntity<SimpleResponse> signUp(@RequestBody MemberEntity member) {
		log.debug("signup member : {}", member);
		memberService.save(member);
		return ResponseEntity.ok(SimpleResponse.builder().message("회원가입이 완료되었습니다.").build());
	}
}
