package com.payment_service.kafka;

import java.util.List;

import lombok.Data;

@Data
public class PaymentFailedEvent {

    private Long orderId;

    private List<PaymentFailedItemEvent> items;
}