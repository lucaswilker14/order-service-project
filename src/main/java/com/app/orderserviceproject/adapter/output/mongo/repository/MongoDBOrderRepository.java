package com.app.orderserviceproject.adapter.output.mongo.repository;

import com.app.orderserviceproject.adapter.output.mongo.repository.entity.OrderEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoDBOrderRepository extends ListCrudRepository<OrderEntity, String> {

    Optional<OrderEntity> findOrderEntityByExternalId(String orderId);

    List<OrderEntity> findOrderEntitiesByStatus(String status);

}
