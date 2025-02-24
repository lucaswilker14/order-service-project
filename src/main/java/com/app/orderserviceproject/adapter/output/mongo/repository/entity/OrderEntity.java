package com.app.orderserviceproject.adapter.output.mongo.repository.entity;

import com.app.orderserviceproject.domain.model.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "order")
public class OrderEntity implements Serializable {

    @Id
    private String id;

    private String externalId;

    private List<OrderItemEntity> items;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private LocalDateTime createdAt;

}
