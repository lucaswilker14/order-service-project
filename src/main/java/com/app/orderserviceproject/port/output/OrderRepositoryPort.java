package com.app.orderserviceproject.port.output;

import com.app.orderserviceproject.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Optional<List<Order>> getOrders();

    Order saveOrder(Order order);

    Order getOrder(String externalId);

    List<Order> findOrderEntitiesByStatus(String status);
}
