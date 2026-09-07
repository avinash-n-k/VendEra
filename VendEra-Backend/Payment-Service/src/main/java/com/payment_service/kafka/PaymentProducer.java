package com.payment_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentProducer {

//    private final KafkaTemplate<String, PaymentProcessedEvent> kafkaTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;
	
    public void sendPaymentProcessedEvent(
            PaymentProcessedEvent event)
    {
        kafkaTemplate.send("payment-processed-events", event);

        System.out.println(
            "Sent PaymentProcessedEvent to Kafka : " + event
        );
    }
    
    public void sendPaymentFailedEvent(PaymentFailedEvent event)
    {
        kafkaTemplate.send("payment-failed-events", event);

        System.out.println(
            "Sent PaymentFailedEvent to Kafka : " + event
        );
    }
}