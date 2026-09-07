package com.order_service.dto;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class SuccessResponse {
	
	private String message;
	private Integer status;
	private Object data;
	private LocalDateTime timeStamp; 

}
