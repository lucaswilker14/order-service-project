package com.app.orderserviceproject.domain.service;

import com.app.orderserviceproject.adapter.output.redis.RedisIdempotencyPort;
import com.app.orderserviceproject.domain.exceptions.OrderDuplicateException;
import com.app.orderserviceproject.domain.exceptions.OrderNotFoundException;
import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.domain.model.OrderItem;
import com.app.orderserviceproject.domain.model.OrderStatus;
import com.app.orderserviceproject.port.input.OrderServicePort;
import com.app.orderserviceproject.port.output.KafkaOrderPort;
import com.app.orderserviceproject.port.output.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderService implements OrderServicePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepositoryPort orderRepositoryPort;

    private final KafkaOrderPort kafkaOrderPort;

    private final RedisIdempotencyPort idempotencyPort;


    public OrderService(OrderRepositoryPort orderRepositoryPort, KafkaOrderPort kafkaOrderPort,
                        RedisIdempotencyPort idempotencyPort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.kafkaOrderPort = kafkaOrderPort;
        this.idempotencyPort = idempotencyPort;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepositoryPort.getOrders()
                .filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new OrderNotFoundException("No orders found"));
    }

    @Override
    public Order createOrder(Order order, String idempotencyKey) throws Exception {

        LOGGER.info("Starting order creation with idempotencyKey: {}", idempotencyKey);
        Order orderIdemKey = getCachedOrderIfExists(idempotencyKey);
        if (orderIdemKey != null) throw new OrderDuplicateException(idempotencyKey, orderIdemKey);

        LOGGER.info("Calculating Total Order Price...");
        BigDecimal totalPrice = calculateTotalOrderPrice(order);
        order.setTotalPrice(totalPrice);

        LOGGER.info("Changing Order Status to: {}" , OrderStatus.PROCESSED);
        order.setStatus(OrderStatus.PROCESSED);

        Order orderSaved = saveOrder(order);

        LOGGER.info("Starting Send Order to kafka: {}", orderSaved);
        var isCompleteSend = kafkaOrderPort.sendOrder(orderSaved);
        if (isCompleteSend)  orderSaved.setStatus(OrderStatus.SHIPPED);

        LOGGER.info("Starting Save Order in Cache: {}", orderSaved);
        saveOrderInCache(idempotencyKey, orderSaved);

        return orderSaved;

    }

    @Override
    public Order getOrder(String externalId) {
        return Optional.ofNullable(orderRepositoryPort.getOrder(externalId))
                .orElseThrow(() -> new OrderNotFoundException("Order not found: externalId=" + externalId));
    }

    @Override
    public List<Order> findOrderEntitiesByStatus(String status) {
        String statusUpper = status.toUpperCase().trim();

        OrderStatus orderStatus = OrderStatus.fromString(statusUpper);

        return Optional.ofNullable(orderRepositoryPort.findOrderEntitiesByStatus(statusUpper))
                .filter(orders -> !orders.isEmpty())
                .orElseThrow(() -> new OrderNotFoundException("No orders found for status: " + statusUpper));
    }

    private Order saveOrder(Order order) {
        return orderRepositoryPort.saveOrder(order);
    }

    private static BigDecimal calculateTotalOrderPrice(Order order) {

        if (order.getItems() == null || order.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return order.getItems()
                .stream()
                .map(OrderService::calculateItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateItemTotalPrice(OrderItem orderItem) {
        BigDecimal price = orderItem.getPrice() != null ? orderItem.getPrice() : BigDecimal.ZERO;
        int quantity = orderItem.getQuantity() != null ? orderItem.getQuantity() : 0;
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    private Order getCachedOrderIfExists(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) return null;
        return idempotencyPort.validadeIdempotencyKey(idempotencyKey);
    }

    private void saveOrderInCache(String idempotencyKey, Order order) {
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) return;
        idempotencyPort.saveOrderCache(idempotencyKey, order);
    }

}
