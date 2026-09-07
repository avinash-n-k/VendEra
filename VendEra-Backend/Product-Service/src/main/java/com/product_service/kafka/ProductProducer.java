package com.product_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductProducer {
	
//	private final KafkaTemplate<String, StockReservedEvent> kafkaTemplate;
//	
//	public void sendStockReservedEvent(StockReservedEvent event)
//	{
//		kafkaTemplate.send("stock-reserved-events", event);
//		
//		System.out.println("Sent StockReservedEvent to Kafka : " + event);
//	}
	
	
    private final KafkaTemplate<String, Object> kafkaTemplate;
	
	public void sendStockReservedEvent(StockReservedEvent event)
	{
		kafkaTemplate.send("stock-reserved-events", event);
		
		System.out.println("Sent StockReservedEvent to Kafka : " + event);
	}
	
	public void sendStockReservationFailedEvent(StockReservationFailedEvent event)
	{
		kafkaTemplate.send("stock-reservation-failed-events", event);
		
		System.out.println("Sent StockReservationFailedEvent "+event);
	}
	
	
	public void sendStockRestoredEvent(StockRestoredEvent event)
	{
		kafkaTemplate.send("stock-restored-event",event);
		
		System.out.println("Sent StockRestoredEvent "+event);
		
	}

}
