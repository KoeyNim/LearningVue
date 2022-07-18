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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieCommon {
	
	public boolean readCountCookie(HttpServletResponse response, HttpServletRequest request, Long id) {
		Cookie[] cookies = request.getCookies();
		Map<String, String> mapCookie = new HashMap<>();
		// 쿠키값 map형식으로 변환
		if (ObjectUtils.isNotEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				mapCookie.put(cookie.getName(), cookie.getValue());
			}
		}
		// readCount Null 체크 후 여부에 따라 랜덤 값 생성 및 기존 값 불러옴
		String cookieReadCount = StringUtils.isBlank(mapCookie.get("readCount"))
				? UUID.randomUUID().toString().replaceAll("-", "")
				: mapCookie.get("readCount");
		// 저장될 새로운 값
		String newCookieReadCount = "|" + id;
		// 쿠키에 새로 입력할 구분값이 존재하는 지 검사
		if (StringUtils.indexOfIgnoreCase(cookieReadCount, newCookieReadCount) == -1) {
			// 없을 경우 구분값 삽입
			Cookie cookie = new Cookie("readCount", cookieReadCount + newCookieReadCount);
			log.info("readCount value : {}",cookie.getValue());
			cookie.setComment("게시글 조회 확인"); // 해당 쿠키가 어떤 용도인지 커멘트
			cookie.setMaxAge(60 * 60 * 24); // 해당 쿠키의 유효시간을 설정 (초 기준)
			response.addCookie(cookie); // 쿠키 생성
			return true;
		}
		return false;
	}
}
