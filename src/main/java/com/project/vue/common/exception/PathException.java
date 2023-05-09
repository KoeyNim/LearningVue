package com.project.vue.common.exception;

import lombok.Getter;

@Getter
public class PathException extends RuntimeException {

	private static final long serialVersionUID = 2768286907717527202L;
	private ErrorCode errorCode;

	public PathException(String message, ErrorCode errorCode) {
		super(message);
	    this.errorCode = errorCode;
	}

	public PathException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause);
	    this.errorCode = errorCode;
	}
}
