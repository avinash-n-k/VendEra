package com.product_service.kafka;

import lombok.Data;

@Data
public class OrderItemEvent {

    private Long productId;

    private Long quantity;
}
