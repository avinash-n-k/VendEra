package com.payment_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.payment_service.service.PaymentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentConsumer {

//    private PaymentProducer paymentProducer;
	private PaymentService pService;

    @KafkaListener(
        topics = "stock-reserved-events",
        groupId = "payment-group"
    )
    public void consume(StockReservedEvent event) throws Exception
    {
        System.out.println("Payment-Service received: " + event);

       pService.processPayment(event);
    }
}