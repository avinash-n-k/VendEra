package com.order_service.kafka;

import java.util.List;

import lombok.Data;

@Data
public class OrderCreatedEvent {
	
	
	private Long orderId;
	private List<OrderItemEvent> items;
	

}
