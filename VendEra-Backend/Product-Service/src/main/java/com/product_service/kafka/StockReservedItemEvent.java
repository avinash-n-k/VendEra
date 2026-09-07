package com.product_service.kafka;

import lombok.Data;

@Data
public class StockReservedItemEvent {

    private Long productId;

    private Long quantity;

    private Double amount;
}