package com.project.vue.common;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PathController {

	private final ResourceLoader resourceLoader;
	private final ThymeleafProperties thymeleafProperties;

	@GetMapping
	public String root(Model model) {
		model.addAttribute("userId", SecurityContextHolder.getContext().getAuthentication().getName());
		return "index";
	}

	/**
	 * @param page 페이지 경로
	 * @param auth 로그인 데이터
	 * @return String
	 */
	@GetMapping("{page}")
	public String page(@PathVariable String page, Authentication auth) {
		log.debug("page: {}", page);
		String view = null;

		if (page.contains("board")) {
			view = "board/" + page;
		}

		if (page.contains("member")) {
			view = "member/" + page;
		}

		Resource resource = resourceLoader.getResource(
				thymeleafProperties.getPrefix() + view + thymeleafProperties.getSuffix());
		log.trace("{}", resource.getDescription());

		if(!resource.exists()) {
			throw new RuntimeException("Not Found Page : " + page); // TODO Exception..
		}

		if (page.contains("member") && !ObjectUtils.isEmpty(auth)) {
			view = "redirect:board";
		}
		return view;
	}
}
