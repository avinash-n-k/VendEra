package com.order_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.order_service.service.OrderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentProcessedConsumer {
	
	private OrderService oService;
	
	@KafkaListener(topics = "payment-processed-events",groupId = "order-group")
	public void consume(PaymentProcessedEvent event)
	{
		System.out.println("Received Payment Processed Event : "+event);
		
		oService.confirmOrder(event.getOrderId());
		
	}

}
