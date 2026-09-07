package com.order_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.order_service.service.OrderService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class StockRestoredConsumer {
	
	private OrderService oService;
	
	@KafkaListener(topics = "stock-restored-events",groupId = "order-group")
	public void consume(StockRestoredEvent event)
	{
		System.out.println("Received StockRestoredEvent "+event);
		
		oService.failOrder(event.getOrderId());
	}

}
