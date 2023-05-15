package com.project.vue.common;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.vue.common.exception.ErrorCode;
import com.project.vue.common.exception.PathException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PathController {

	private final ResourceLoader resourceLoader;
	private final ThymeleafProperties thymeleafProperties;

	/**
	 * root url
	 * @param model
	 * @return String
	 */
	@GetMapping
	public String root(Model model) {
		model.addAttribute("userId", SecurityContextHolder.getContext().getAuthentication().getName());
		return "index";
	}

	/**
	 * page url
	 * @param page 페이지 경로
	 * @return String
	 */
	@GetMapping("{page}")
	public String page(@PathVariable String page, Model model) {
		log.debug("page: {}", page);
		String path = getPath(page);
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		/** resource 경로 */
		Resource resource = resourceLoader.getResource(
				thymeleafProperties.getPrefix() + path + thymeleafProperties.getSuffix());
		log.trace("@@ PathController resource: {}", resource.getDescription());

		if(!resource.exists()) {
			throw new PathException("Not Found page : " + page, ErrorCode.NOT_FOUND);
		}

		if (page.contains(PathConstants.MEMBER) && !userId.equals("anonymousUser")) {
			path = "redirect:board";
		}

		model.addAttribute("userId", userId);
		return path;
	}

	/**
	 * path
	 * @param page 페이지 경로
	 * @return String view
	 */
	private String getPath(String page) {
		if (page.contains(PathConstants.BOARD)) return PathConstants.BOARD + "/" + page;
		if (page.contains(PathConstants.MEMBER)) return PathConstants.MEMBER + "/" + page;
		return null;
	}
}
