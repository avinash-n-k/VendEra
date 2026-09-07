package com.order_service.kafka;

import lombok.Data;

@Data
public class StockRestoredEvent {

    private Long orderId;
    private Long productId;
}