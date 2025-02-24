package com.app.orderserviceproject.adapter.output.mongo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Document(collection = "orderItem")
public class OrderItemEntity implements Serializable {

    @Id
    private Integer productId;

    private String productName;

    private Integer quantity;

    private BigDecimal price;


}
