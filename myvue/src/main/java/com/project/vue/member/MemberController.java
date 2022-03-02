package com.project.vue.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
		boolean r = true;
		try {
			memberService.save(member);
		} catch (Exception e) {
			e.printStackTrace();
			r = false;
		}
		return ResponseEntity.ok(SimpleResponse.builder()
					.success(r)
					.message(r ? "회원가입이 완료되었습니다." : "회원가입에 실패하였습니다.")
					.build());
	}
	
	@ResponseBody
	@PostMapping("login")
	public ResponseEntity<SimpleResponse> logIn(@RequestBody MemberEntity member) {
		boolean r = true;
		try {
			memberService.logIn(member);
		} catch (Exception e) {
			e.printStackTrace();
			r = false;
		}
		return ResponseEntity.ok(SimpleResponse.builder()
					.success(r)
					.message(r ? "로그인 성공" : "로그인에 실패하였습니다.")
					.build());
	}
	
	  @GetMapping(value = "/logout")
	  public String logOut(HttpServletRequest request, HttpServletResponse response) {
	    new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
	    return "redirect:/member/member-login";
	  }
}
