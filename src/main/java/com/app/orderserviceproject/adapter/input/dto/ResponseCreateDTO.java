package com.app.orderserviceproject.adapter.input.dto;

import com.app.orderserviceproject.domain.model.OrderStatus;


public record ResponseCreateDTO(String idempotencyKey, OrderStatus orderStatus) {

}
