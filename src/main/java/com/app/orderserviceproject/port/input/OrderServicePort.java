package com.app.orderserviceproject.port.input;

import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.domain.model.OrderItem;
import com.app.orderserviceproject.domain.model.OrderStatus;

import java.util.List;

public interface OrderServicePort {

    List<Order> getOrders();

    Order createOrder(Order order, String idempotencyKey) throws Exception;

    Order getOrder(String externalId);

    List<Order> findOrderEntitiesByStatus(String status);

}
