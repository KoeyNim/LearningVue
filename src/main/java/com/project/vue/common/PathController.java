package com.project.vue.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.vue.board.BoardEntity;
import com.project.vue.board.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PathController {
	
	private final CookieCommon cookieCommon;
	
	private final BoardService boardService;

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
	public String boardDetail(HttpServletResponse response, HttpServletRequest request, Authentication authentication, @RequestParam Long id) {
		BoardEntity board = boardService.findById(id);
		if (!board.getUserId().equals(authentication.getPrincipal())) {
			cookieCommon.readCountCookie(response, request, id);
		}
		return "board/board-detail";
	}
}
