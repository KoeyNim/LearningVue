package com.project.vue.user.member;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vue.common.Constants;
import com.project.vue.common.PathConstants;
import com.project.vue.common.SimpleResponse;
import com.project.vue.user.payload.MemberSignUpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_PREFIX+ "/" + PathConstants.API_MEMBER)
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping(PathConstants.API_USER_ID_CHECK)
	public ResponseEntity<SimpleResponse> idChk(String userId) {
		log.debug("api/v1/member/idchk - gets - userId : {}", userId);
		return 	memberService.isUserId(userId) ? ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse.builder()
				.success(false).message("이미 사용중인 ID 입니다.").statusCode(409).build())
				: ResponseEntity.ok(SimpleResponse.builder().message("사용 가능한 ID 입니다.").build());
	}

	@PostMapping(PathConstants.API_USER_SIGNUP)
	public ResponseEntity<SimpleResponse> signUp(@RequestBody MemberSignUpRequest req) {
		log.debug("api/v1/member/signup - posts - req : {}", req);
		memberService.save(req);
		return ResponseEntity.ok(SimpleResponse.builder().message("회원가입이 완료되었습니다.").build());
	}
}
