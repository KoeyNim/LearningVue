package com.project.vue.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.project.vue.board.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieCommon {

	private final BoardService boardService;
	
	public HttpServletResponse readCountCookie(HttpServletResponse response, HttpServletRequest request, Long id) {
		// 저장된 쿠키 불러오기
		Cookie cookies[] = request.getCookies();
		Map<String, String> mapCookie = new HashMap<>();
		if (ObjectUtils.isNotEmpty(request.getCookies())) {
			for (Cookie cookie : cookies) {
				Cookie obj = cookie;
				mapCookie.put(obj.getName(), obj.getValue());
			}
		}

		// 저장된 쿠키중에 read_count 만 불러오기
		String cookie_read_count = StringUtils.isBlank(mapCookie.get("read_count"))
				? UUID.randomUUID().toString().replaceAll("-", "")
				: mapCookie.get("read_count");
		// 저장될 새로운 쿠키값 생성
		String new_cookie_read_count = "|" + id;

		// 저장된 쿠키에 새로운 쿠키값이 존재하는 지 검사
		if (StringUtils.indexOfIgnoreCase(cookie_read_count, new_cookie_read_count) == -1) {
			// 없을 경우 쿠키 생성
			Cookie cookie = new Cookie("read_count", cookie_read_count + new_cookie_read_count);
			log.debug("cookie@@{}",cookie.getValue());
			cookie.setComment("게시글 조회 확인"); // 해당 쿠키가 어떤 용도인지 커멘트
			cookie.setMaxAge(60 * 60 * 24); // 해당 쿠키의 유효시간을 설정 (초 기준)
			response.addCookie(cookie); // 쿠키 생성
			// 조회수 업데이트
			boardService.saveCount(id);
		}
		return response;
	}
}
