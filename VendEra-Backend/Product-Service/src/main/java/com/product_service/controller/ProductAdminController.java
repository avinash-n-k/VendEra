package com.product_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product_service.dto.SuccessResponse;
import com.product_service.entity.Product;
import com.product_service.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/products")
@AllArgsConstructor
public class ProductAdminController {
	
	private ProductService pService;
	
	
	@PostMapping
	public ResponseEntity<SuccessResponse> addProduct(@RequestBody Product product)
	{
	    return pService.addProduct(product);
	}

}
