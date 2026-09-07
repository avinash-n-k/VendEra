package com.product_service.exception;

public class SomethingWentWrongException extends RuntimeException{
	
	public SomethingWentWrongException(String message)
	{
		super(message);
	}

}
