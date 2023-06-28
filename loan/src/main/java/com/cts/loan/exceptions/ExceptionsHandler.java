package com.cts.loan.exceptions;

import java.time.LocalDateTime;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cts.loan.model.ApiError;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
	
	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
	       return new ResponseEntity<>(apiError, apiError.getStatus());
	   }

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,LocalDateTime.now(),"Invalid Token"));
	}
	
	@ExceptionHandler(LoanNotFoundException.class)
	public ResponseEntity<Object> handleLoanNotFoundException(LoanNotFoundException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,LocalDateTime.now(),"Loan Not Found"));
	}
	
	@ExceptionHandler(LoanApplicationsNotFoundException.class)
	public ResponseEntity<Object> handleLoanApplicationsNotFoundException(LoanApplicationsNotFoundException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,LocalDateTime.now(),"Loan Applications Not Found"));
	}
	
	@ExceptionHandler(CustomersAbove5LNotFoundException.class)
	public ResponseEntity<Object> handleCustomersAbove5LNotFoundException(CustomersAbove5LNotFoundException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,LocalDateTime.now(),"Customers with Loan Amount above 5L Not Found"));
	}
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Object> handleCustomerNotFoundException(CustomerNotFoundException ce) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,LocalDateTime.now(),"Customer Not Found"));
	}

}
