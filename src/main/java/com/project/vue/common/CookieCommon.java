package com.project.vue.common;

import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieCommon {

	/**
	 * 조회수 쿠키 생성
	 * @param seqno 기본키
	 * @return boolean 조회수 증가 여부
	 */
	public boolean isReadCountCookie(long seqno) {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

		String cookieName = "readCnt";
		String newValue = "|" + seqno;
		/** cookieName의 value를 가져옴, 없을시 쿠키 기본값 생성 */
		String cookieStr = Arrays.stream(req.getCookies())
				.filter(e -> StringUtils.equals(e.getName(), cookieName))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(UUID.randomUUID().toString().replaceAll("-", ""));
		log.debug("Cookie Value : {}", cookieStr);

		/** 쿠키에 새로 입력할 구분값이 존재하는 지 검사 */
		if (StringUtils.indexOfIgnoreCase(cookieStr, newValue) == -1) {
			Cookie cookie = new Cookie(cookieName, cookieStr + newValue);
			log.debug("readCount value : {}", cookie.getValue());
			cookie.setComment("조회수 중복 체크"); // 쿠키 설명
			cookie.setMaxAge(60 * 60 * 24); // 유효시간 설정 (초)
			res.addCookie(cookie);
			return true;
		}
		return false;
	}
}
