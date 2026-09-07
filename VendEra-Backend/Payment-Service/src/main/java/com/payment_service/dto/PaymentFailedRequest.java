package com.payment_service.dto;

import lombok.Data;

@Data
public class PaymentFailedRequest {

    private Long orderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
}
