package com.order_service.dto;

import lombok.Data;

@Data
public class OrderItemResponse {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private Double price;
    private Long quantity;
}
