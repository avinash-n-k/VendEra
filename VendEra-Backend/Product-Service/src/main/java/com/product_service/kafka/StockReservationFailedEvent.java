package com.product_service.kafka;

import java.util.List;

import lombok.Data;

@Data
public class StockReservationFailedEvent {
	
	private Long orderId;
	private List<StockReservationFailedItemEvent> items;

}
