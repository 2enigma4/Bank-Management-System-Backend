package com.cts.authentication.exceptions;

import java.time.LocalDateTime;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cts.authentication.model.ApiError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
	       return new ResponseEntity<>(apiError, apiError.getStatus());
	   }

	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,LocalDateTime.now(),"Invalid Credentials"));
	}
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN,LocalDateTime.now(),"Username Already Exists"));
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,LocalDateTime.now(),"Invalid Token"));
	}
	
	
}
