package com.project.vue.common.exception;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice // 컨트롤러 전역에서 발생할 수 있는 예외를 잡아 Throw
public class CustomExceptionHandler {
	
	/**
	 * BizException Handler
	 * @param ex BizException
	 * @param req HttpServletRequest
	 * @return ResponseEntity<ErrorResponse>
	 */
	@ExceptionHandler(BizException.class) // 특정 클래스에서 발생할 수 있는 예외를 잡아 Throw
	private String bizExceptionHandler(BizException ex, HttpServletRequest req, HttpServletResponse res , Model model) {
		log.error("@@ BizException ", ex);
		model.addAttribute("errorResponse", new ErrorResponse(ex, req));
		res.setStatus(ex.getErrorCode().getStatus());
		return "error/";
	}
//	@ExceptionHandler(BizException.class) // 특정 클래스에서 발생할 수 있는 예외를 잡아 Throw
//	private ResponseEntity<ErrorResponse> bizExceptionHandler(BizException ex, HttpServletRequest req) {
//		log.error("@@ BizException ", ex);
//		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(new ErrorResponse(ex, req));
//	}
	
//	@ExceptionHandler(BizException.class) // TODO thymeleaf 에러페이지 이동할 수 있도록 변경
//	private String bizExceptionHandler(BizException ex, HttpServletRequest req) {
//		log.error("@@ BizException ", ex);
//
//		ErrorResponse res = ErrorResponse.builder()
//							.status(ex.getErrorCode().getStatus())
//							.error(ex.getErrorCode().getError())
//							.message(ex.getMessage())
//							.path(req.getRequestURI()).build();
//
//		return "error/"+ ex.getErrorCode().getStatus();
//	}

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
     * Error Response Static Class
     * @author KoeyNim-이민혁
     */
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ErrorResponse {
    	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    	private LocalDateTime timestamp = LocalDateTime.now();
    	private int status;
    	private String error;
    	private String message;
    	private String path;

    	private ErrorResponse(BizException ex, HttpServletRequest req) {
            this.status = ex.getErrorCode().getStatus();
            this.error = ex.getErrorCode().getError();
            this.message = ex.getMessage();
            this.path = req.getRequestURI();
        }
    }
}
