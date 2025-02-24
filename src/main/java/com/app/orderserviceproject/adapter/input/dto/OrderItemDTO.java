package com.app.orderserviceproject.adapter.input.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderItemDTO(Integer productId,
                           String productName,
                           Integer quantity,
                           BigDecimal price) implements Serializable {
}
