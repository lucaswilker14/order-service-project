package com.app.orderserviceproject.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {

    private String id;

    private String externalId;

    private List<OrderItem> items;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private LocalDateTime createdAt;

    public Order(String id, String externalId, List<OrderItem> items, BigDecimal totalPrice, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.externalId = externalId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id)
                && Objects.equals(externalId, order.externalId)
                && Objects.equals(items, order.items)
                && Objects.equals(totalPrice, order.totalPrice)
                && status == order.status
                && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, externalId, items, totalPrice, status, createdAt);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", externalId='" + externalId + '\'' +
                ", items=" + items +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
