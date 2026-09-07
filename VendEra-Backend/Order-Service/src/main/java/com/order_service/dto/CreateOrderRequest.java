package com.order_service.dto;


import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private List<OrderItemRequest> items;
}