package com.order_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.order_service.service.OrderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StockReservationFailedEventConsumer {
	
	private OrderService oService;
	
	@KafkaListener(topics = "stock-reservation-failed-events",groupId = "order-group")
	public void consume(StockReservationFailedEvent event)
	{
		System.out.println(
	            "Received StockReservationFailedEvent: " + event
	        );

	        oService.failOrder(event.getOrderId());
	}

}
