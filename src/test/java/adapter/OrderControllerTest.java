package adapter;

import com.app.orderserviceproject.adapter.input.controller.OrderController;
import com.app.orderserviceproject.adapter.input.dto.OrderDTO;
import com.app.orderserviceproject.adapter.input.dto.ResponseCreateDTO;
import com.app.orderserviceproject.adapter.mapper.OrderMapper;
import com.app.orderserviceproject.domain.exceptions.OrderStatusInvalidException;
import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.domain.model.OrderStatus;
import com.app.orderserviceproject.port.input.OrderServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderServicePort orderServicePort;

    @InjectMocks
    private OrderController orderController;

    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        order = new Order("1", "ORDER-12345", Collections.emptyList(),
                BigDecimal.TEN, OrderStatus.RECEIVED, LocalDateTime.now());
        orderDTO = OrderMapper.INSTANCE.toDto(order);
    }

    @Test
    void getOrders_ShouldReturnListOfOrders() {
        when(orderServicePort.getOrders()).thenReturn(List.of(order));

        ResponseEntity<List<OrderDTO>> response = orderController.getOrders();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        assertEquals(orderDTO.externalId(), response.getBody().get(0).externalId());

        verify(orderServicePort, times(1)).getOrders();
    }

    @Test
    void createOrder_ShouldReturnResponseCreateDTO() throws Exception {
        String idempotencyKey = "unique-key-34542144";

        when(orderServicePort.createOrder(any(), eq(idempotencyKey))).thenReturn(order);

        ResponseEntity<ResponseCreateDTO> response = orderController.createOrder(orderDTO, idempotencyKey);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(idempotencyKey, Objects.requireNonNull(response.getBody()).idempotencyKey());
        assertEquals(OrderStatus.RECEIVED, response.getBody().orderStatus());

        verify(orderServicePort, times(1)).createOrder(any(), eq(idempotencyKey));
    }

    @Test
    void getOrder_ShouldReturnOrderDTO() {
        when(orderServicePort.getOrder(order.getExternalId())).thenReturn(order);

        ResponseEntity<OrderDTO> response = orderController.getOrder(order.getExternalId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(orderDTO.externalId(), response.getBody().externalId());

        verify(orderServicePort, times(1)).getOrder(order.getExternalId());
    }

    @Test
    void findOrderEntitiesByStatus_ShouldReturnListOfOrders() {
        when(orderServicePort.findOrderEntitiesByStatus("RECEIVED")).thenReturn(List.of(order));

        ResponseEntity<?> response = orderController.findOrderEntitiesByStatus("RECEIVED");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertInstanceOf(List.class, response.getBody());
        assertFalse(((List<?>) response.getBody()).isEmpty());

        verify(orderServicePort, times(1)).findOrderEntitiesByStatus("RECEIVED");
    }

    @Test
    void findOrderEntitiesByStatus_InvalidStatus_ShouldReturnBadRequest() {
        String invalidStatus = "INVALID";

        when(orderServicePort.findOrderEntitiesByStatus(invalidStatus))
                .thenThrow(new OrderStatusInvalidException(invalidStatus));

        ResponseEntity<?> response = orderController.findOrderEntitiesByStatus(invalidStatus);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid order status: " + invalidStatus, response.getBody());

        verify(orderServicePort, times(1)).findOrderEntitiesByStatus(invalidStatus);
    }

}
