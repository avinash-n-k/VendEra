package com.order_service.kafka;

import lombok.Data;

@Data
public class OrderItemEvent {

    private Long productId;

    private Long quantity;
}