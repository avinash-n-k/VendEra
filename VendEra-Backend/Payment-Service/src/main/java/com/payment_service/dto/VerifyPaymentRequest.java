package com.payment_service.dto;



import lombok.Data;

@Data
public class VerifyPaymentRequest {

    private Long orderId;

    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String razorpaySignature;
}