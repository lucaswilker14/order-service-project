package com.app.orderserviceproject.adapter.input.controller;

import com.app.orderserviceproject.adapter.input.dto.OrderDTO;
import com.app.orderserviceproject.adapter.input.dto.ResponseCreateDTO;
import com.app.orderserviceproject.adapter.mapper.OrderMapper;
import com.app.orderserviceproject.domain.exceptions.OrderStatusInvalidException;
import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.domain.model.OrderStatus;
import com.app.orderserviceproject.port.input.OrderControllerInputPort;
import com.app.orderserviceproject.port.input.OrderServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController implements OrderControllerInputPort {

    private final OrderServicePort orderServicePort;

    public OrderController(OrderServicePort orderServicePort) {
        this.orderServicePort = orderServicePort;
    }

    @Override
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderServicePort.getOrders();
        List<OrderDTO> orderDTOs = orders.stream().map(OrderMapper.INSTANCE::toDto).toList();

        return ResponseEntity.ok(orderDTOs);
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseCreateDTO> createOrder(@RequestBody OrderDTO orderDTO,
                                                         @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey) throws Exception {
        Order createNewOrder = orderServicePort.createOrder(OrderMapper.INSTANCE.toDomain(orderDTO), idempotencyKey);
        return ResponseEntity.ok(new ResponseCreateDTO(idempotencyKey, OrderStatus.RECEIVED));
    }

    @Override
    @GetMapping("/order/{externalId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String externalId) {
        return ResponseEntity.ok(OrderMapper.INSTANCE.toDto(orderServicePort.getOrder(externalId)));
    }

    @Override
    @GetMapping("/status/{status}")
    public ResponseEntity<?> findOrderEntitiesByStatus(@PathVariable String status) {
        try {
            List<Order> orderEntitiesByStatus = orderServicePort.findOrderEntitiesByStatus(status);
            return ResponseEntity.ok(orderEntitiesByStatus.stream().map(OrderMapper.INSTANCE::toDto).toList());

        } catch (OrderStatusInvalidException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
