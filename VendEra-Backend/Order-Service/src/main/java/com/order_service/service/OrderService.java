package com.order_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order_service.dto.CreateOrderRequest;
import com.order_service.dto.OrderDetailsResponse;
import com.order_service.dto.OrderItemRequest;
import com.order_service.dto.OrderItemResponse;
import com.order_service.dto.OrderResponse;
import com.order_service.dto.SuccessResponse;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.entity.OutboxEvent;
import com.order_service.entity.ProductResponse;
import com.order_service.kafka.OrderCreatedEvent;
import com.order_service.kafka.OrderItemEvent;
import com.order_service.kafka.OrderProducer;
import com.order_service.openfeign.ProductClient;
import com.order_service.repository.OrderRepository;
import com.order_service.repository.OutboxEventRepository;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class OrderService {
	
	private OrderRepository oRepo;
	private ProductClient pClient;
	private OrderProducer oProducer;
	private OutboxEventRepository outboxEventRepo;
	private ObjectMapper objectMapper;
	private EmailService emailService;

	
////	@Transactional
//	public Order createOrder(Long pid,Long quantity)
//	{
//		Order order=new Order();
//		order.setProductId(pid);
//		order.setQuantity(quantity);
//		order.setStatus("PENDING");		
//		Order savedOrder=oRepo.save(order);
////		pClient.reduceStock(pid);
//		OrderCreatedEvent event=new OrderCreatedEvent();
//		event.setOrderId(savedOrder.getId());
//		event.setProductId(savedOrder.getProductId());
//		event.setQuantity(savedOrder.getQuantity());
//		oProducer.sendOrderEvent(event);
//		return savedOrder;
//	}
	
//	@Transactional
//	public ResponseEntity<SuccessResponse> createOrder(Long pid,Long quantity,String userEmail)
//	{
//		Order order=new Order();
//
//		order.setStatus("PENDING");
//		order.setUserEmail(userEmail);
//		Order savedOrder=oRepo.save(order);
//		
//		
//		OrderCreatedEvent event=new OrderCreatedEvent();
//		event.setOrderId(savedOrder.getId());
//		event.setProductId(savedOrder.getProductId());
//		event.setQuantity(savedOrder.getQuantity());
//		
//		
//		
//			String payload=objectMapper.writeValueAsString(event);
//			
//			OutboxEvent outboxEvent=new OutboxEvent();
//			outboxEvent.setEventType("OrderCreatedEvent");
//			outboxEvent.setAggregateId(savedOrder.getId());
//			outboxEvent.setTopic("order-events");
//			outboxEvent.setPayload(payload);
//			outboxEvent.setStatus("PENDING");
//			
//			outboxEventRepo.save(outboxEvent);
//		
//		OrderResponse orderresponse=new OrderResponse();
//		orderresponse.setOrderId(savedOrder.getId());
//		orderresponse.setProductId(savedOrder.getProductId());
//		orderresponse.setQuantity(savedOrder.getQuantity());
//		orderresponse.setStatus(savedOrder.getStatus());
//		orderresponse.setCreatedAt(LocalDateTime.now());
//		
//		
//		SuccessResponse successresponse=new SuccessResponse("Order Initiated",HttpStatus.CREATED.value(),orderresponse,LocalDateTime.now());
//		
//		return new ResponseEntity<SuccessResponse>(successresponse,HttpStatus.CREATED);
//		
//	}
	
	
	@Transactional
	public ResponseEntity<SuccessResponse> createOrder(CreateOrderRequest request,String userEmail) {

	    Order order = new Order();

	    order.setStatus("PENDING");
	    order.setUserEmail(userEmail);

	    List<OrderItem> orderItems = new ArrayList<>();

	    for (OrderItemRequest itemRequest : request.getItems()) {

	    	ProductResponse product=pClient.getProduct(itemRequest.getProductId());
	        
	    	OrderItem orderItem = new OrderItem();

	        orderItem.setProductId(itemRequest.getProductId());
	        orderItem.setProductName(product.getName());
	        orderItem.setPrice(product.getPrice());
	        orderItem.setQuantity(itemRequest.getQuantity());
	        orderItem.setOrder(order);

	        orderItems.add(orderItem);
	    }

	    order.setItems(orderItems);

	    Order savedOrder = oRepo.save(order);


	    List<OrderItemEvent> eventItems = new ArrayList<>();

	    for (OrderItemRequest itemRequest : request.getItems()) {
	    	
	    	

	        OrderItemEvent itemEvent = new OrderItemEvent();

	        itemEvent.setProductId(itemRequest.getProductId());
	        itemEvent.setQuantity(itemRequest.getQuantity());

	        eventItems.add(itemEvent);
	    }


	    OrderCreatedEvent event = new OrderCreatedEvent();

	    event.setOrderId(savedOrder.getId());
	    event.setItems(eventItems);


	    String payload = objectMapper.writeValueAsString(event);

	    OutboxEvent outboxEvent = new OutboxEvent();

	    outboxEvent.setEventType("OrderCreatedEvent");
	    outboxEvent.setAggregateId(savedOrder.getId());
	    outboxEvent.setTopic("order-events");
	    outboxEvent.setPayload(payload);
	    outboxEvent.setStatus("PENDING");

	    outboxEventRepo.save(outboxEvent);


	    OrderResponse orderResponse = new OrderResponse();

	    orderResponse.setOrderId(savedOrder.getId());
	    orderResponse.setStatus(savedOrder.getStatus());
	    orderResponse.setCreatedAt(LocalDateTime.now());

	    SuccessResponse successResponse =
	            new SuccessResponse(
	                    "Order Initiated",
	                    HttpStatus.CREATED.value(),
	                    orderResponse,
	                    LocalDateTime.now()
	            );

	    return new ResponseEntity<>(
	            successResponse,
	            HttpStatus.CREATED
	    );
	}
	
	
//	public void confirmOrder(Long orderId)
//	{
//		Order order=oRepo.findById(orderId).orElseThrow(()->new RuntimeException("Order Not Found"));
//		order.setStatus("SUCCESS");
//		oRepo.save(order);
//	}
//	
	public void confirmOrder(Long orderId)
	{
	    Order order = oRepo.findOrderWithItems(orderId);

	    if (order == null) {
	        throw new RuntimeException("Order Not Found");
	    }

	    order.setStatus("SUCCESS");

	    oRepo.save(order);

	    emailService.sendOrderConfirmation(order);
	}
	
//	public void failOrder(Long orderId)
//	{
//		Order order=oRepo.findById(orderId).orElseThrow(()->new RuntimeException("Order Not Found"));
//		order.setStatus("FAILED");
//		oRepo.save(order);
//	}
	
	public void failOrder(Long orderId)
	{
	    Order order = oRepo.findOrderWithItems(orderId);

	    if (order == null) {
	        throw new RuntimeException("Order Not Found");
	    }

	    if ("FAILED".equals(order.getStatus())) {
	        return;
	    }

	    order.setStatus("FAILED");

	    oRepo.save(order);

	    emailService.sendOrderFailure(order);
	}
	
	
//	public List<Order> getAllOrders()
//	{
//	    return oRepo.findAll();
//	}
//	
	public List<OrderDetailsResponse> getAllOrders() {

	    List<Order> orders = oRepo.findAll();

	    List<OrderDetailsResponse> responses = new ArrayList<>();

	    for (Order order : orders) {

	        OrderDetailsResponse response = new OrderDetailsResponse();

	        response.setOrderId(order.getId());
	        response.setStatus(order.getStatus());
	        response.setUserEmail(order.getUserEmail());

	        List<OrderItemResponse> itemResponses = new ArrayList<>();

	        for (OrderItem item : order.getItems()) {

	            OrderItemResponse itemResponse = new OrderItemResponse();

	            itemResponse.setOrderItemId(item.getId());
	            itemResponse.setProductId(item.getProductId());
	            itemResponse.setProductName(item.getProductName());
	            itemResponse.setPrice(item.getPrice());
	            itemResponse.setQuantity(item.getQuantity());

	            itemResponses.add(itemResponse);
	        }

	        response.setItems(itemResponses);

	        responses.add(response);
	    }

	    return responses;
	}
	
	
	
//	public List<Order> getMyOrders(String userEmail) {
//	    return oRepo.findByUserEmail(userEmail);
//	}
	
	
	public List<OrderDetailsResponse> getMyOrders(String userEmail) {

	    List<Order> orders = oRepo.findByUserEmail(userEmail);

	    List<OrderDetailsResponse> responses = new ArrayList<>();

	    for (Order order : orders) {

	        OrderDetailsResponse response = new OrderDetailsResponse();

	        response.setOrderId(order.getId());
	        response.setStatus(order.getStatus());
	        response.setUserEmail(order.getUserEmail());

	        List<OrderItemResponse> itemResponses = new ArrayList<>();

	        for (OrderItem item : order.getItems()) {

	            OrderItemResponse itemResponse = new OrderItemResponse();

	            itemResponse.setOrderItemId(item.getId());
	            itemResponse.setProductId(item.getProductId());
	            itemResponse.setProductName(item.getProductName());
	            itemResponse.setPrice(item.getPrice());
	            itemResponse.setQuantity(item.getQuantity());

	            itemResponses.add(itemResponse);
	        }

	        response.setItems(itemResponses);

	        responses.add(response);
	    }

	    return responses;
	}
	
	
//	private OrderDetailsResponse convertToOrderDetailsResponse(Order order) {
//
//	    OrderDetailsResponse response = new OrderDetailsResponse();
//
//	    response.setOrderId(order.getId());
//	    response.setStatus(order.getStatus());
//	    response.setUserEmail(order.getUserEmail());
//
//	    List<OrderItemResponse> itemResponses = new ArrayList<>();
//
//	    for (OrderItem item : order.getItems()) {
//
//	        OrderItemResponse itemResponse = new OrderItemResponse();
//
//	        itemResponse.setOrderItemId(item.getId());
//	        itemResponse.setProductId(item.getProductId());
//	        itemResponse.setProductName(item.getProductName());
//	        itemResponse.setPrice(item.getPrice());
//	        itemResponse.setQuantity(item.getQuantity());
//
//	        itemResponses.add(itemResponse);
//	    }
//
//	    response.setItems(itemResponses);
//
//	    return response;
//	}
	
	
	
//	public List<OrderDetailsResponse> getMyOrders(String userEmail) {
//
//	    List<Order> orders = oRepo.findByUserEmail(userEmail);
//
//	    List<OrderDetailsResponse> responses = new ArrayList<>();
//
//	    for (Order order : orders) {
//	        responses.add(convertToOrderDetailsResponse(order));
//	    }
//
//	    return responses;
//	}
	
	
//	public List<OrderDetailsResponse> getAllOrders() {
//
//	    List<Order> orders = oRepo.findAll();
//
//	    List<OrderDetailsResponse> responses = new ArrayList<>();
//
//	    for (Order order : orders) {
//	        responses.add(convertToOrderDetailsResponse(order));
//	    }
//
//	    return responses;
//	}
	
	

}
