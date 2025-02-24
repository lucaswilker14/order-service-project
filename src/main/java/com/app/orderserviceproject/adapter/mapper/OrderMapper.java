package com.app.orderserviceproject.adapter.mapper;

import com.app.orderserviceproject.adapter.input.dto.OrderDTO;
import com.app.orderserviceproject.adapter.output.mongo.repository.entity.OrderEntity;
import com.app.orderserviceproject.domain.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class );

    OrderDTO toDto(Order order); // domain -> dto
    Order toDomain(OrderDTO dto); // dto -> domain

    Order toDomain(OrderEntity entity); //entity -> domain
    OrderEntity toEntity(Order order); // domain -> entity

    default Order toDomainFromMap(Map<String, Object> map) {
        try {
            return new ObjectMapper().convertValue(map, Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Order from map", e);
        }
    }

}
