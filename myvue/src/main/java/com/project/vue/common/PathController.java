package com.project.vue.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PathController {
	
	private final CookieCommon cookieCommon;

	@GetMapping
	public String home() {
		return "index";
	}
	
	@GetMapping("login")
	public String signIn() {
		return "member/member-login";
	}
	
	@GetMapping("signup")
	public String signUp() {
		return "member/member-signup";
	}

	@GetMapping("board")
	public String board() {
		return "board/board";
	}
	
	@GetMapping("board/regist")
	public String boardRegist() {
		return "board/board-form";
	}
	
	@GetMapping("board/detail")
	public String boardDetail(HttpServletResponse response, HttpServletRequest request, @RequestParam Long id) {
		cookieCommon.readCountCookie(response, request, id);
		return "board/board-detail";
	}
}
