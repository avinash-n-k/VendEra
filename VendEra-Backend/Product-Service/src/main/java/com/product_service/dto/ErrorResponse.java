package com.product_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private Integer status;
	private String message;
	private LocalDateTime timeStamp;

}
