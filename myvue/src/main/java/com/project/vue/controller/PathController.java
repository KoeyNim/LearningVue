package com.project.vue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {

	@GetMapping
	public String home() {
		return "index";
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
