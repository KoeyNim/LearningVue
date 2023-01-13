package com.project.vue.common.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
	
	/**
	 * BizException Handler
	 * @param ex BizException
	 * @param req HttpServletRequest
	 * @return ResponseEntity<ErrResponse>
	 */
	@ExceptionHandler(BizException.class)
	private ResponseEntity<ErrResponse> bizExceptionHandler(BizException ex, HttpServletRequest req) {
		log.error("@@ BizException ", ex);

		ErrResponse res = ErrResponse.builder()
							.status(ex.getErrorCode().getStatus())
							.error(ex.getErrorCode().getError())
							.message(ex.getMessage())
							.path(req.getRequestURI()).build();

		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(res);
	}

    /**
     * RuntimeException Handler
     * @param ex RuntimeException
     * @return ResponseEntity<?>
     */
    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> runtimeExceptionHandler(RuntimeException ex){
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
     * Error Code enum
     * @author KoeyNim-이민혁
     */
    @Getter
    @JsonFormat(shape = Shape.OBJECT)
    @RequiredArgsConstructor
    public enum ErrorCode {

    	BAD_REQUEST(400, "Bad Request"),
    	UNAUTHORIZED(401, "Unauthorized"),
    	NOT_FOUND(404, "Not Found"),
    	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    	SERVICE_UNAVAILABLE(503, "Service Unavailable");

    	private final int status;
    	private final String error;
    	
    }

    /**
     * Error Response Static Class
     * @author KoeyNim-이민혁
     */
    @Builder
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ErrResponse {
    	@Builder.Default
    	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    	private LocalDateTime timestamp = LocalDateTime.now();
    	private int status;
    	private String error;
    	private String message;
    	private String path;
    }
}
