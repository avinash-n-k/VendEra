package com.product_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.product_service.entity.ProcessedEvent;
import com.product_service.repository.ProcessedEventRepository;
import com.product_service.service.ProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentFailedConsumer {
	
	private ProductService pService;
	private ProcessedEventRepository processedEventRepo;
	
	
//	@KafkaListener(topics = "payment-failed-events",groupId = "product-group")
//	public void consume(PaymentFailedEvent event)
//	{
//		System.out.println(
//	            "Product-Service received Payment Failed Event : " + event
//	        );
//
//	    pService.restoreStock(event.getProductId(),event.getOrderId(),event.getQuantity());
//	}
	
	
	@KafkaListener(topics = "payment-failed-events",groupId = "product-group")
	public void consume(PaymentFailedEvent event)
	{
		System.out.println(
	            "Product-Service received Payment Failed Event : " + event
	        );
		
		System.out.println("Payment failed for Order " + event.getOrderId());
		
		
		pService.processPaymentFailed(event);
	}
	
	
}
