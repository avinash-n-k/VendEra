package com.payment_service.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment_service.dto.PaymentFailedRequest;
import com.payment_service.dto.VerifyPaymentRequest;
import com.payment_service.entity.Payment;
import com.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    @GetMapping("/{orderId}")
    public Payment getPaymentByOrderId(@PathVariable Long orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }
    
    
    @PostMapping("/verify")
    public Payment verifyPayment(@RequestBody VerifyPaymentRequest request) throws Exception {

        return paymentService.verifyPayment(request);
    }
    
    @PostMapping("/fail")
    public Payment failPayment(@RequestBody PaymentFailedRequest request) throws Exception {

        return paymentService.failPayment(request);
    }
}
