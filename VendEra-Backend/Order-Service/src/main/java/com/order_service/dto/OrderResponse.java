package com.order_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderResponse {
	
	private Long orderId;
	private String status;
	private LocalDateTime createdAt;
}
