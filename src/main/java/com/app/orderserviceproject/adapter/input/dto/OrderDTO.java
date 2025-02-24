package com.app.orderserviceproject.adapter.input.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record OrderDTO(String id, String externalId, List<OrderItemDTO>items,
                        BigDecimal totalPrice, LocalDateTime createdAt) implements Serializable {
}
