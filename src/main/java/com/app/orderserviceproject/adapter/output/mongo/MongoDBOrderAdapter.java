package com.app.orderserviceproject.adapter.output.mongo;

import com.app.orderserviceproject.adapter.mapper.OrderMapper;
import com.app.orderserviceproject.adapter.output.mongo.repository.MongoDBOrderRepository;
import com.app.orderserviceproject.adapter.output.mongo.repository.entity.OrderEntity;
import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.port.output.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MongoDBOrderAdapter implements OrderRepositoryPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBOrderAdapter.class);

    @Autowired
    private MongoDBOrderRepository repository;

    @Override
    public Optional<List<Order>> getOrders() {

        LOGGER.info("Finding All Orders in Database...");

        Optional<List<OrderEntity>> orderEntities = Optional.of(repository.findAll());

        return orderEntities.map(orderEntities1 -> orderEntities1
                        .stream()
                        .map(OrderMapper.INSTANCE::toDomain)
                .collect(Collectors.toList()));
    }

    @Override
    @CacheEvict(value = "orders", key = "#order.externalId")
    public Order saveOrder(Order order) {

        OrderEntity entity = repository.save(OrderMapper.INSTANCE.toEntity(order));
        LOGGER.info("Saved the order on MongoDB: {}", order);

        return OrderMapper.INSTANCE.toDomain(entity);
    }

    @Override
    @Cacheable(value = "orders", key = "#externalId", unless = "#result == null")
    public Order getOrder(String externalId) {

        OrderEntity orderEntity = repository.findOrderEntityByExternalId(externalId).orElse(null);
        LOGGER.info("Found Order with externalId: {}", orderEntity);

        return OrderMapper.INSTANCE.toDomain(orderEntity);
    }

    @Override
    @Cacheable(value = "orders", key = "#status", unless = "#result == null or #result.isEmpty()")
    public List<Order> findOrderEntitiesByStatus(String status) {
        LOGGER.info("Buscando Order pelo status {} no banco...", status);
        LOGGER.info("Finding Order by Status...");

        return repository.findOrderEntitiesByStatus(status)
                .stream()
                .map(OrderMapper.INSTANCE::toDomain)
                .toList();
    }

}
