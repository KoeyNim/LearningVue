package com.project.vue.common.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice // 전역 예외처리
public class CustomExceptionHandler {

	/**
	 * BizException Handler
	 * @param ex BizException
	 * @param req HttpServletRequest
	 * @return ResponseEntity<ErrorResponse>
	 */
	@ExceptionHandler(BizException.class)
	private ResponseEntity<ErrorResponse> bizExceptionHandler(BizException ex, HttpServletRequest req) {
		log.error("@@ BizException ", ex);
		ErrorResponse res = ErrorResponse.builder()
							.status(ex.getErrorCode().getStatus())
							.error(ex.getErrorCode().getError())
							.message(ex.getMessage())
							.path(req.getRequestURI()).build();

		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(res);
	}

	/**
	 * PathException Handler
	 * @param ex PathException
	 * @return String
	 */
	@ExceptionHandler(PathException.class)
	private String pathExceptionHandler(PathException ex, HttpServletRequest req, Model model) {
		log.error("@@ PathException {}", ex);
		String path = "error/" + ErrorCode.statusOf(ex.getErrorCode().getStatus());
		ErrorResponse res = ErrorResponse.builder()
							.status(ex.getErrorCode().getStatus())
							.error(ex.getErrorCode().getError())
							.message(ex.getMessage())
							.path(req.getRequestURI()).build();

		model.addAttribute("errorResponse", res);
		return path;
	}

    /**
     * RuntimeException Handler
     * @param ex RuntimeException
     * @return ResponseEntity<?>
     */
    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> runtimeExceptionHandler(RuntimeException ex) {
    	log.error("@@ RuntimeException ", ex);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Exception Handler
     * @param ex RuntimeException
     * @return ResponseEntity<?>
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> exceptionHandler(Exception ex) {
    	log.error("@@ Exception ", ex);
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Error Response Static Class
     * @author KoeyNim-이민혁
     */
    @Builder
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ErrorResponse {
    	@Builder.Default
    	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    	private LocalDateTime timestamp = LocalDateTime.now();
    	private int status;
    	private String error;
    	private String message;
    	private String path;
    }
}
