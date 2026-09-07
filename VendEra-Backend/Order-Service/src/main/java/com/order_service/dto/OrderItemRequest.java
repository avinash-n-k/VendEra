package com.order_service.dto;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long productId;

    private Long quantity;
}