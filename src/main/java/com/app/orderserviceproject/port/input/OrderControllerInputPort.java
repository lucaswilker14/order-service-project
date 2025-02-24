package com.app.orderserviceproject.port.input;

import com.app.orderserviceproject.adapter.input.dto.OrderDTO;
import com.app.orderserviceproject.adapter.input.dto.ResponseCreateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderControllerInputPort {

    ResponseEntity<List<OrderDTO>> getOrders();

    ResponseEntity<ResponseCreateDTO> createOrder(OrderDTO order, String idempotencyKey) throws Exception;

    ResponseEntity<OrderDTO> getOrder(String id);

    ResponseEntity<?> findOrderEntitiesByStatus(String status);

}
