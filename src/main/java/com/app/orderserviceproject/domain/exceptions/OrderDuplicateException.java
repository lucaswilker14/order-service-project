package com.app.orderserviceproject.domain.exceptions;

import com.app.orderserviceproject.domain.model.Order;

public class OrderDuplicateException extends RuntimeException {

    public OrderDuplicateException(String idempotencyKey, Order order) {
        super(String.format("Idempotency key %s already exists in Redis. Created At: %s",
                idempotencyKey, order.getCreatedAt().toString()));
    }
}
