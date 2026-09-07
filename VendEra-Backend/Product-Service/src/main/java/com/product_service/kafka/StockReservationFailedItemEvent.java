package com.product_service.kafka;

import lombok.Data;

@Data
public class StockReservationFailedItemEvent {

    private Long productId;

    private Long quantity;
}