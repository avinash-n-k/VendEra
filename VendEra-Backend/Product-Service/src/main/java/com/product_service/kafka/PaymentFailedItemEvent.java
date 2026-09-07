package com.product_service.kafka;

import lombok.Data;

@Data
public class PaymentFailedItemEvent {
	
	private Long productId;
    private Long quantity;

}
