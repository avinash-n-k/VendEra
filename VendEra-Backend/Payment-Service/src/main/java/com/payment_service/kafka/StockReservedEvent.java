package com.payment_service.kafka;

import java.util.List;

import lombok.Data;

@Data
public class StockReservedEvent {
	
	private Long orderId;
	private List<StockReservedItemEvent> items;

}
