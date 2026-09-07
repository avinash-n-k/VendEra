package com.product_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product_service.entity.Product;
import com.product_service.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
	
	
	private ProductService pService;
	
//	private int count=0;
	
//	@Value("${server.port}")
//	private int port;
//	
//	@Value("${message}")
//	private String message;
	
//	@GetMapping("/{id}")
////	@RequestHeader(value = "X-Gateway-Request",required = false)String gateWayRequest
//	public Product getProductById(@PathVariable Long id) throws InterruptedException
//	{
////		count++;
////		System.out.println("Attempt :"+count);
//		
//		
////		if(count==1)
////		{
////			throw new RuntimeException("Temporary Product Service Failure");
////		}
////		throw new RuntimeException("Temporary Product Service Failure");
////		Thread.sleep(10000);  
////		System.out.println("X-GateWay-Request "+gateWayRequest);
//		System.out.println("Request handled by Product-Service : " + port);
//		System.out.println(message);
//		return new Product(id,"Laptop",90000.00);
//	}
	
	
	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id)
	{
		return pService.getProductById(id);
	}
	
	
//	@PutMapping("/{pid}/stock")
//	public Product reduceStock(@PathVariable Long pid)
//	{
//		System.out.println("Entered");
//		return pService.reduceStock(pid);
//	}
	
	@GetMapping
	public List<Product> getAllProducts()
	{
	    return pService.getAllProducts();
	}
	
	
	
}
