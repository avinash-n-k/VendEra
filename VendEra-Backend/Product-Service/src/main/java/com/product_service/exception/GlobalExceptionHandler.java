package com.product_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.product_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(SomethingWentWrongException.class)
	public ResponseEntity<ErrorResponse> handleSomethingWentWrongException(SomethingWentWrongException ex)
	{
		ErrorResponse response=new ErrorResponse();
		response.setMessage(ex.getMessage());
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setTimeStamp(LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.CONFLICT);
	}

}
