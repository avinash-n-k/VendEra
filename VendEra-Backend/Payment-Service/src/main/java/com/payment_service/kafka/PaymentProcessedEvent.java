package com.payment_service.kafka;

import lombok.Data;

@Data
public class PaymentProcessedEvent {
	
	private Long orderId;
	private Long productId;
}
