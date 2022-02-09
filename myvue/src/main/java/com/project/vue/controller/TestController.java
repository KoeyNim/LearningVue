package com.project.vue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
	@GetMapping("/")
	public String home() {
		
		return "index";
	}
	
	@GetMapping("/test")
	public String home2() {
		
		return "test/index";
	}
}
