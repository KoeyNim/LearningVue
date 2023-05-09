package com.project.vue.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error Code Enum
 * @author KoeyNim-이민혁
 */
@Getter
@JsonFormat(shape = Shape.OBJECT)
@RequiredArgsConstructor
public enum ErrorCode {

	BAD_REQUEST(400, "Bad Request"),
//	UNAUTHORIZED(401, "Unauthorized"),
	NOT_FOUND(404, "Not Found"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	SERVICE_UNAVAILABLE(503, "Service Unavailable");

	private final int status;
	private final String error;

	public static Object statusOf(int status) {
		for (var errorCode : ErrorCode.values()) {
			if (errorCode.getStatus() == status) return status;
		}
		if (status >= 400 && status <= 499) return "4xx";
		if (status >= 500 && status <= 599) return "5xx";
		return null;
	}
	
}
