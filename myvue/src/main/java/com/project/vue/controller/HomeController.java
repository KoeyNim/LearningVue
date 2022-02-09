package com.project.vue.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	
	@GetMapping("/")
	public String home() {
		String aa = "11";
		log.debug("####{}",aa);
		return "index";
	}
}
