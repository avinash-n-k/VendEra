package com.order_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetailsResponse {

    private Long orderId;
    private String status;
    private String userEmail;
    private List<OrderItemResponse> items;
}