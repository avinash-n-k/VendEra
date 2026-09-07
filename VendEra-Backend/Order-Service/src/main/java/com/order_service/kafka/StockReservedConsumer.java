//package com.order_service.kafka;
//
//
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import com.order_service.service.OrderService;
//
//import lombok.AllArgsConstructor;
//
//@Service
//@AllArgsConstructor
//public class StockReservedConsumer {
//
//    private OrderService orderService;
//
//    @KafkaListener(
//        topics = "stock-reserved-events",
//        groupId = "order-group"
//    )
//    public void consume(StockReservedEvent event)
//    {
//        System.out.println(
//            "Received StockReservedEvent: " + event
//        );
//
//        orderService.confirmOrder(event.getOrderId());
//    }
//}