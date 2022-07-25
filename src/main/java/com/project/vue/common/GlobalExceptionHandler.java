package com.project.vue.common;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 400 BadRequest
    @ExceptionHandler(RuntimeException.class)
    private void badRequestExceptionHandler(final RuntimeException e, HttpServletResponse response) throws IOException {
        log.warn("error ", e);
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    // 401 AccessDenied
    @ExceptionHandler(AccessDeniedException.class)
    private void accessDeniedExceptionHandler(final AccessDeniedException e, HttpServletResponse response) throws IOException {
    	log.warn("error ", e);
    	response.sendError(HttpStatus.UNAUTHORIZED.value());
    }

    // 500 InternalServerError
    @ExceptionHandler(Exception.class)
    private void exceptionHandler(final Exception e, HttpServletResponse response) throws IOException {
        log.info("Exception Class Name : {}",e.getClass().getName());
        log.error("error ", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

//    // 400 BadRequest
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<SimpleResponse> badRequestExceptionHandler(final RuntimeException e) {
//        log.warn("error ", e);
//        return ResponseEntity
//        		    .badRequest()
//        			.body(SimpleResponse.builder()
//        			.success(false)
//        			.statusCode(HttpStatus.BAD_REQUEST.value())
//        			.message(e.getLocalizedMessage())
//        			.build());
//    }
//
//    // 401 AccessDenied
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<SimpleResponse> accessDeniedExceptionHandler(final AccessDeniedException e) {
//    	log.warn("error ", e);
//        return ResponseEntity
//	        		.status(HttpStatus.UNAUTHORIZED)
//	    			.body(SimpleResponse.builder()
//	    			.success(false)
//	    			.statusCode(HttpStatus.UNAUTHORIZED.value())
//	    			.message(e.getLocalizedMessage())
//	    			.build());
//    }
//
//    // 500 InternalServerError
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<SimpleResponse> exceptionHandler(final Exception e) {
//        log.info("Exception Class Name : {}",e.getClass().getName());
//        log.error("error ", e);
//        return ResponseEntity
//	    			.internalServerError()
//	    			.body(SimpleResponse.builder()
//	    			.success(false)
//	    			.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//	    			.message(e.getLocalizedMessage())
//	    			.build());
//    }
}
