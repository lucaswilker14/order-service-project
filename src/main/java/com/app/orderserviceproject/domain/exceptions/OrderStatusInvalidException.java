package com.app.orderserviceproject.domain.exceptions;

public class OrderStatusInvalidException extends RuntimeException {

    public OrderStatusInvalidException(String status) {
        super("Invalid order status: " + status);
    }
}
