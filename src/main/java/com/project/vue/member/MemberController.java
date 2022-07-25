package com.project.vue.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.vue.common.Constants;
import com.project.vue.common.SimpleResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX+"/member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@ResponseBody
	@PostMapping("signup")
	public ResponseEntity<SimpleResponse> signUp(@RequestBody MemberEntity member) {
		log.debug("signup member : {}", member);
		memberService.save(member);
		return ResponseEntity.ok(SimpleResponse.builder().message("회원가입이 완료되었습니다.").build());
	}
	
	@PostMapping("checkid")
	public ResponseEntity<SimpleResponse> checkId(@RequestBody String userId) {
		log.debug("userId : {}", userId);
		return 	memberService.findUserId(userId).isEmpty()
				? ResponseEntity.ok(SimpleResponse.builder().message("사용 가능한 ID 입니다.").build()) 
				: ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse.builder()
													.success(false)
													.message("이미 사용중인 ID 입니다.")
													.statusCode(409).build());
	}
}
