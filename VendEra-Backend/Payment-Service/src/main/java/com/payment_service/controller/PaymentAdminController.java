package com.payment_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment_service.entity.Payment;
import com.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/payments")
@AllArgsConstructor
public class PaymentAdminController {

    private PaymentService paymentService;

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }
}
