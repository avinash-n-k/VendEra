package com.product_service.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product_service.dto.SuccessResponse;
import com.product_service.entity.OutboxEvent;
import com.product_service.entity.ProcessedEvent;
import com.product_service.entity.Product;
import com.product_service.exception.SomethingWentWrongException;
import com.product_service.kafka.OrderCreatedEvent;
import com.product_service.kafka.OrderItemEvent;
import com.product_service.kafka.PaymentFailedEvent;
import com.product_service.kafka.PaymentFailedItemEvent;
import com.product_service.kafka.ProductProducer;
import com.product_service.kafka.StockReservedEvent;
import com.product_service.kafka.StockReservedItemEvent;
import com.product_service.kafka.StockRestoredEvent;
import com.product_service.repository.OutboxEventRepository;
import com.product_service.repository.ProcessedEventRepository;
import com.product_service.repository.ProductRepository;

import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class ProductService {
	
	private ProductRepository productRepo;
	private ProcessedEventRepository processedEventRepo;
	private ProductProducer productProducer;
	private ObjectMapper objectMapper;
	private OutboxEventRepository outboxEventRepo;
	
	
	public Product getProductById(Long id)
	{
		Product product=productRepo.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));
		
		return product;
	}
	
	
//	@Transactional
//	public void processOrderCreated(OrderCreatedEvent event)
//	{
//		if(processedEventRepo.existsByOrderId(event.getOrderId()))
//		{
//			System.out.println("Event Already Proccessed for Order "+event.getOrderId());
//			return;
//		}
//		
//		
//			
//		
//		Product product=reduceStock(event.getProductId(),event.getQuantity());
//		
//		ProcessedEvent processedEvent=new ProcessedEvent();
//		processedEvent.setOrderId(event.getOrderId());
//		
//		processedEventRepo.save(processedEvent);
//		
//		
//		StockReservedEvent stockreservedevent=new StockReservedEvent();
//		stockreservedevent.setOrderId(event.getOrderId());
//		stockreservedevent.setProductId(event.getProductId());
//		stockreservedevent.setQuantity(event.getQuantity());
//		stockreservedevent.setAmount(product.getPrice()*event.getQuantity());
//		
//		String payload=objectMapper.writeValueAsString(stockreservedevent);
//		
//		OutboxEvent outboxEvent=new OutboxEvent();
//		outboxEvent.setAggregateId(event.getOrderId());
//		outboxEvent.setEventType("StockReservedEvent");
//		outboxEvent.setStatus("PENDING");
//		outboxEvent.setTopic("stock-reserved-events");
//		outboxEvent.setPayload(payload);
//		
//		outboxEventRepo.save(outboxEvent);
//		
//		
//		
//		
//		
//		
////		productProducer.sendStockReservedEvent(stockreservedevent);
//		
//		
//		
//	}
	
	
	@Transactional
	public void processOrderCreated(OrderCreatedEvent event)
	{
	    if (processedEventRepo.existsByOrderIdAndEventType(event.getOrderId(),"OrderCreatedEvent"))
	    {
	        System.out.println(
	            "Event Already Proccessed for Order "
	            + event.getOrderId()
	        );

	        return;
	    }

	    StockReservedEvent stockReservedEvent =
	            new StockReservedEvent();

	    stockReservedEvent.setOrderId(event.getOrderId());

	    List<StockReservedItemEvent> reservedItems = new ArrayList<>();


	    for (OrderItemEvent item : event.getItems())
	    {
	        Product product =
	                reduceStock(
	                    item.getProductId(),
	                    item.getQuantity()
	                );

	        StockReservedItemEvent reservedItem =
	                new StockReservedItemEvent();

	        reservedItem.setProductId(item.getProductId());
	        reservedItem.setQuantity(item.getQuantity());

	        reservedItem.setAmount(
	            product.getPrice() * item.getQuantity()
	        );

	        reservedItems.add(reservedItem);
	    }


	    stockReservedEvent.setItems(reservedItems);


	    ProcessedEvent processedEvent =
	            new ProcessedEvent();

	    processedEvent.setOrderId(event.getOrderId());
	    processedEvent.setEventType("OrderCreatedEvent");

	    processedEventRepo.save(processedEvent);


	    String payload =
	            objectMapper.writeValueAsString(stockReservedEvent);

	    OutboxEvent outboxEvent =
	            new OutboxEvent();

	    outboxEvent.setAggregateId(event.getOrderId());
	    outboxEvent.setEventType("StockReservedEvent");
	    outboxEvent.setStatus("PENDING");
	    outboxEvent.setTopic("stock-reserved-events");
	    outboxEvent.setPayload(payload);

	    outboxEventRepo.save(outboxEvent);
	}
	
	public Product reduceStock(Long pid,Long quantity)
	{
		Product product=productRepo.findById(pid).orElseThrow(()->new RuntimeException("Product Not Found"));
		
		if(product.getStock()<=0 || quantity>product.getStock())
		{
			throw new RuntimeException("Insufficinet Stock");
		}
		
		product.setStock(product.getStock()-quantity);
			

	    productRepo.save(product);
			
//		throw new RuntimeException("Product Service Failed after stock update");
		return product;
	}
	
	
	@Transactional
	public Product restoreStock(Long pid,Long oid,Long quantity)
	{
		Product product=productRepo.findById(pid).orElseThrow(()->new RuntimeException("Product Not Found"));
		
		product.setStock(product.getStock()+quantity);
		
		productRepo.save(product);
		
		StockRestoredEvent event=new StockRestoredEvent();
		event.setProductId(pid);
		event.setOrderId(oid);
		
		
		String payload = objectMapper.writeValueAsString(event);

		OutboxEvent outboxEvent = new OutboxEvent();

		outboxEvent.setAggregateId(oid);
		outboxEvent.setEventType("StockRestoredEvent");
		outboxEvent.setStatus("PENDING");
		outboxEvent.setTopic("stock-restored-events");
		outboxEvent.setPayload(payload);

		outboxEventRepo.save(outboxEvent);
		
//		productProducer.sendStockRestoredEvent(event);
		
		
		return product;
	}
	
	
	public List<Product> getAllProducts()
	{
	    return productRepo.findAll();
	}
	
	public ResponseEntity<SuccessResponse> addProduct(Product product)
	{
		Product productresponse=productRepo.save(product);
	    if(productresponse!=null)
	    {
	    	SuccessResponse response=new SuccessResponse();
	    	response.setStatus(HttpStatus.CREATED.value());
	    	response.setMessage("Product Added SuccessFully");
	    	response.setTimeStamp(LocalDateTime.now());
	    	
	    	return new ResponseEntity<SuccessResponse>(response,HttpStatus.CREATED);
	    }
	    else
	    {
	    	throw new SomethingWentWrongException("Failed to add Product");
	    }
	}
	
	@Transactional
	public void processPaymentFailed(PaymentFailedEvent event)
	{
		if (processedEventRepo.existsByOrderIdAndEventType(
		        event.getOrderId(),
		        "PaymentFailedEvent"))
		{
		    return;
		}

	    
	    
	    
       
	    for (PaymentFailedItemEvent item : event.getItems())
	    {
	        restoreStock(
	            item.getProductId(),
	            event.getOrderId(),
	            item.getQuantity()
	        );
	    }

	    ProcessedEvent processedEvent = new ProcessedEvent();

	    processedEvent.setOrderId(event.getOrderId());
	    processedEvent.setEventType("PaymentFailedEvent");

	    processedEventRepo.save(processedEvent);
	}

}
