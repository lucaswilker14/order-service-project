package com.app.orderserviceproject.domain.model;

import com.app.orderserviceproject.domain.exceptions.OrderStatusInvalidException;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum OrderStatus {

    RECEIVED, PROCESSED, SHIPPED, PENDING;

    @JsonCreator
    public static OrderStatus fromString(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(e -> e.toString().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new OrderStatusInvalidException(status));
    }

    public static void validateStatus(OrderStatus status) {
        boolean isValid = Arrays.stream(OrderStatus.values())
                .anyMatch(e -> e.toString().equalsIgnoreCase(String.valueOf(status)));

        if (!isValid) throw new OrderStatusInvalidException(status.toString());
    }
}
