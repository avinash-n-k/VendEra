package com.product_service.kafka;

import java.util.ArrayList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.product_service.entity.OutboxEvent;
import com.product_service.entity.ProcessedEvent;
import com.product_service.repository.OutboxEventRepository;
import com.product_service.repository.ProcessedEventRepository;
import com.product_service.service.ProductService;

import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class ProductConsumer {
	
	private ProductService pService;
	private ProductProducer productProducer;
	private ObjectMapper objectMapper;
	private OutboxEventRepository outboxEventRepo;
//	private ProcessedEventRepository processedEventRepo;

	
//	@KafkaListener(topics = "order-events",groupId = "product-group")
//	public void consume(String message )
//	{
//		System.out.println("Received from Kafka: " + message);
//	}
	
//	@KafkaListener(topics = "order-events",groupId = "product-group")
//	public void consume(OrderCreatedEvent event )
//	{
//		System.out.println("Received from Kafka: " + event);
//		
//		if(processedEventRepo.existsByOrderId(event.getOrderId()))
//		{
//			System.out.println("Event Already Proccessed for Order "+event.getOrderId());
//			return;
//		}
//		
//		pService.reduceStock(event.getProductId());
//		
//		
//		
//		ProcessedEvent processedEvent=new ProcessedEvent();
//		processedEvent.setOrderId(event.getOrderId());
//		processedEventRepo.save(processedEvent);
//	}
	
//	@KafkaListener(topics = "order-events", groupId = "product-group")
//	public void consume(OrderCreatedEvent event)
//	{
//	    System.out.println("Received from Kafka: " + event);
//
//	    try
//	    {
//	        pService.processOrderCreated(event);
//	    }
//	    catch (RuntimeException ex)
//	    {
//	        System.out.println(
//	            "Stock reservation failed for Order "
//	            + event.getOrderId()
//	        );
//
//	        StockReservationFailedEvent failedEvent =
//	                new StockReservationFailedEvent();
//
//	        failedEvent.setOrderId(event.getOrderId());
//	        failedEvent.setProductId(event.getProductId());
//	        failedEvent.setQuantity(event.getQuantity());
//	        
//	        String payload=objectMapper.writeValueAsString(failedEvent);
//	        
//	        OutboxEvent outboxevent=new OutboxEvent();
//	        outboxevent.setAggregateId(event.getOrderId());
//	        outboxevent.setEventType("StockReservationFailedEvent");
//	        outboxevent.setStatus("PENDING");
//	        outboxevent.setTopic("stock-reservation-failed-events");
//	        outboxevent.setPayload(payload);
//	        outboxEventRepo.save(outboxevent);
//
////	        productProducer.sendStockReservationFailedEvent(failedEvent);
//	    }
//	}
	
	@KafkaListener(topics = "order-events", groupId = "product-group")
	public void consume(OrderCreatedEvent event)
	{
	    System.out.println("Received from Kafka: " + event);

	    try
	    {
	        pService.processOrderCreated(event);
	    }
	    catch (RuntimeException ex)
	    {
	        System.out.println(
	            "Stock reservation failed for Order "
	            + event.getOrderId()
	        );

	        StockReservationFailedEvent failedEvent =
	                new StockReservationFailedEvent();

	        failedEvent.setOrderId(event.getOrderId());

	        List<StockReservationFailedItemEvent> failedItems =
	                new ArrayList<>();

	        for (OrderItemEvent item : event.getItems())
	        {
	            StockReservationFailedItemEvent failedItem =
	                    new StockReservationFailedItemEvent();

	            failedItem.setProductId(item.getProductId());
	            failedItem.setQuantity(item.getQuantity());

	            failedItems.add(failedItem);
	        }

	        failedEvent.setItems(failedItems);
	        
	        
	        String payload = objectMapper.writeValueAsString(failedEvent);

	        OutboxEvent outboxevent = new OutboxEvent();

	        outboxevent.setAggregateId(event.getOrderId());
	        outboxevent.setEventType("StockReservationFailedEvent");
	        outboxevent.setStatus("PENDING");
	        outboxevent.setTopic("stock-reservation-failed-events");
	        outboxevent.setPayload(payload);

	        outboxEventRepo.save(outboxevent);

//	        productProducer.sendStockReservationFailedEvent(failedEvent);
	    }
	}
		
		
		

}
