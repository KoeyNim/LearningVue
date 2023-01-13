package com.project.vue.common.exception;

import com.project.vue.common.exception.CustomExceptionHandler.ErrorCode;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 8290261778633064678L;
	private ErrorCode errorCode;

	public BizException(String message, ErrorCode errorCode) {
		super(message);
	    this.errorCode = errorCode;
	}

	public BizException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
	    this.errorCode = errorCode;
	}
}
