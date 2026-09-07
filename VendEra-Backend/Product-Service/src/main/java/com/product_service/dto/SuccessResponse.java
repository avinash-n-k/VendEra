package com.product_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SuccessResponse {
	
	private Integer status;
	private String message;
	private LocalDateTime timeStamp;

}
