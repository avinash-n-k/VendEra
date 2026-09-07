package com.order_service.kafka;

import lombok.Data;

@Data
public class StockReservedEvent {

	private Long orderId;
}
