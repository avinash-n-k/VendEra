package com.auth_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth_service.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(PasswordAndConfirmPasswordMissMatchException.class)
	public ResponseEntity<ErrorResponse> handlePasswordAndConfirmPasswordMissMatchException(PasswordAndConfirmPasswordMissMatchException ex)
	{
		ErrorResponse response=new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex)
	{
        ErrorResponse response=new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException ex)
	{
        ErrorResponse response=new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value(), LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleUnAuthorizedException(AccessDeniedException ex)
	{
		ErrorResponse error=new ErrorResponse(ex.getMessage(),HttpStatus.FORBIDDEN.value(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex)
	{
		ErrorResponse error=new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex)
	{
		ErrorResponse error=new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorResponse>(error,HttpStatus.NOT_FOUND);
	}
}
