package com.order_service.kafka;

import lombok.Data;

@Data
public class PaymentProcessedEvent {
	
	private Long orderId;

}
