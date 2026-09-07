package com.order_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order_service.dto.OrderDetailsResponse;
import com.order_service.entity.Order;
import com.order_service.service.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/orders")
@AllArgsConstructor
public class AdminOrderController {

    private OrderService oService;

//    @GetMapping
//    public List<Order> getAllOrders()
//    {
//        return oService.getAllOrders();
//    }
    
    
    @GetMapping
    public List<OrderDetailsResponse> getAllOrders() {
        return oService.getAllOrders();
    }
}
