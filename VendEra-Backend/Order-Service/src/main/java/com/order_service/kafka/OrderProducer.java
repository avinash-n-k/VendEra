package com.order_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderProducer {
	
//	private final KafkaTemplate<String, String> kafkaTemplate;
	
	private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
	
//	public void sendOrderEvent(String message)
//	{
//		kafkaTemplate.send("order-events", message);
//		
//		System.out.println("Sent to Kafka : "+message);
//	}
	
	
	public void sendOrderEvent(OrderCreatedEvent event)
	{
		kafkaTemplate.send("order-events", event);
		System.out.println("Sent OrderCreatedEvent to Kafka : "+event);
	}

}
