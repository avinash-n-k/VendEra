package com.order_service.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.order_service.entity.ProductResponse;

@FeignClient(name = "Product-Service")
public interface ProductClient {
	
	
	@GetMapping("/products/{id}")
	ProductResponse getProduct(@PathVariable Long id);
	
	@PutMapping("/products/{pid}/stock")
	ProductResponse reduceStock(@PathVariable Long pid);

}
