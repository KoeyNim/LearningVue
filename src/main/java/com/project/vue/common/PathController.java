package com.project.vue.common;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PathController {

	@GetMapping
	public String home(Model model) {
		model.addAttribute("userId", SecurityContextHolder.getContext().getAuthentication().getName());
		return "index";
	}

	@GetMapping("login")
	public String signIn(Authentication auth) {
		return ObjectUtils.isEmpty(auth) ? "member/member-login" : "redirect:board";
	}

	@GetMapping("signup")
	public String signUp(Authentication auth) {
		return ObjectUtils.isEmpty(auth) ? "member/member-signup" : "redirect:board";
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
	public String boardDetail() {
		return "board/board-detail";
	}
}
