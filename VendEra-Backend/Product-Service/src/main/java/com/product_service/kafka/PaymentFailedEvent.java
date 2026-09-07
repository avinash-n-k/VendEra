package com.product_service.kafka;



import java.util.List;

import lombok.Data;

@Data
public class PaymentFailedEvent {

    private Long orderId;
    List<PaymentFailedItemEvent> items;
}
