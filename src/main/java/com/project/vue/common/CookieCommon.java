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

import com.project.vue.user.board.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieCommon {

	private final BoardRepository boardRepository;

	/**
	 * 조회수 쿠키 생성 및 검사
	 * @param seqno 기본키
	 */
	public void readCountCookie(long seqno) {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse res = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

		String cookieNm = "readCount";
		String newCookieVal = "|" + seqno;
		/** cookieNm 쿠키의 value를 가져옴, 없을시 쿠키 기본값 생성 */
		String cookieVal = Arrays.stream(req.getCookies())
				.filter(e -> StringUtils.equals(e.getName(), cookieNm))
				.findAny()
				.map(Cookie::getValue)
				.orElse(UUID.randomUUID().toString().replaceAll("-", ""));
		log.debug("cookieVal : {}", cookieVal);

		/** 쿠키에 새로 입력할 구분값이 존재하는 지 검사 */
		if (StringUtils.indexOfIgnoreCase(cookieVal, newCookieVal) == -1) {
			Cookie cookie = new Cookie(cookieNm, cookieVal + newCookieVal);
			log.debug("readCount value : {}", cookie.getValue());
			/** 쿠키 설명 */
			cookie.setComment("조회수 중복 체크");
			/** 유효시간 설정 (초) */
			cookie.setMaxAge(60 * 60 * 24);
			res.addCookie(cookie);
			boardRepository.updateCount(seqno);
		}
	}
}
