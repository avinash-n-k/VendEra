package com.order_service.kafka;

import lombok.Data;

@Data
public class StockReservationFailedEvent {
	
	private Long orderId;

}
