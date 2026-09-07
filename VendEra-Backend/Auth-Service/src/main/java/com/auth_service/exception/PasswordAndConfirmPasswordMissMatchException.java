package com.auth_service.exception;


public class PasswordAndConfirmPasswordMissMatchException extends RuntimeException {

	public PasswordAndConfirmPasswordMissMatchException(String message) {
		
		super(message);
	}
	
	
}
