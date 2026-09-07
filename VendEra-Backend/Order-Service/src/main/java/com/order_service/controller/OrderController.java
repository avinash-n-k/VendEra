package com.order_service.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order_service.dto.CreateOrderRequest;
import com.order_service.dto.OrderDetailsResponse;
import com.order_service.dto.OrderResponse;
import com.order_service.dto.SuccessResponse;
import com.order_service.entity.Order;
import com.order_service.entity.ProductResponse;
import com.order_service.kafka.OrderProducer;
import com.order_service.openfeign.ProductClient;
import com.order_service.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
	
	private final ProductClient pClient;
	private OrderService oService;
	private OrderProducer oProducer;
	
	
	
	@Retry(name="productService")
	@CircuitBreaker(name = "productService",fallbackMethod = "productServiceFallback")
	@RateLimiter(name = "productService")
	@GetMapping("/{id}")
	public ProductResponse getOrderById(@PathVariable Long id)
	{
		 
		System.out.println("🔥 Calling Product-Service...");
		ProductResponse productresponse=pClient.getProduct(id);
		
		productresponse.setOrderId(id);
		return productresponse;
	}
	
	public ProductResponse productServiceFallback(Long id, Throwable ex)
	{
		System.out.println("🔥 FALLBACK EXECUTED");
	    return new ProductResponse(
	        id,
	        null,
	        "Product Service is currently unavailable",
	        0.0
	    );
	}
	
	
//	@PostMapping
//	public ResponseEntity<SuccessResponse> createOrder(@RequestParam Long pid,@RequestParam Long quantity,Authentication authentication)
//	{
//		String userEmail=authentication.getName();
//		return oService.createOrder(pid,quantity,userEmail);
//	}
	
	@PostMapping
	public ResponseEntity<SuccessResponse> createOrder(@RequestBody CreateOrderRequest request  ,Authentication authentication)
	{
		String userEmail=authentication.getName();
		return oService.createOrder(request,userEmail);
	}
	
	
//	@PostMapping("/kafka/test")
//	public String testKafka()
//	{
//		oProducer.sendOrderEvent("Hello From Order Service");
//		
//		return "Message Sent to Kafka";
//	}
//	@GetMapping("/my-orders")
//	public List<Order> getMyOrders(Authentication authentication) {
//
//	    String userEmail = authentication.getName();
//
//	    return oService.getMyOrders(userEmail);
//	}

	
	@GetMapping("/my-orders")
	public List<OrderDetailsResponse> getMyOrders(Authentication authentication) {
	    String userEmail = authentication.getName();
	    return oService.getMyOrders(userEmail);
	}
	
	
}
