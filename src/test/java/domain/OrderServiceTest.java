package domain;

import com.app.orderserviceproject.adapter.output.redis.RedisPort;
import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.domain.model.OrderItem;
import com.app.orderserviceproject.domain.model.OrderStatus;
import com.app.orderserviceproject.domain.service.OrderService;
import com.app.orderserviceproject.port.output.KafkaOrderPort;
import com.app.orderserviceproject.port.output.OrderRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private KafkaOrderPort kafkaOrderPort;

    @Mock
    private RedisPort idempotencyPort;

    @InjectMocks
    private OrderService orderService;

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockOrder = new Order();
        mockOrder.setExternalId("ORDER-123");
        mockOrder.setItems(List.of(new OrderItem(1, "Vinho Branco", 4, BigDecimal.valueOf(25.00))));
        mockOrder.setStatus(OrderStatus.RECEIVED);
    }

    @Test
    void shouldReturnOrdersSuccessfully() {
        when(orderRepositoryPort.getOrders()).thenReturn(Optional.of(List.of(mockOrder)));

        List<Order> orders = orderService.getOrders();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals("ORDER-123", orders.get(0).getExternalId());

        verify(orderRepositoryPort, times(1)).getOrders();
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        String idempotencyKey = "unique-key-123";

        when(idempotencyPort.validadeIdempotencyKey(idempotencyKey)).thenReturn(null);
        when(orderRepositoryPort.saveOrder(any(Order.class))).thenReturn(mockOrder);
        when(kafkaOrderPort.sendOrder(any(Order.class))).thenReturn(true);

        Order createdOrder = orderService.createOrder(mockOrder, idempotencyKey);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.SHIPPED, createdOrder.getStatus());

        verify(orderRepositoryPort, times(1)).saveOrder(any(Order.class));
        verify(kafkaOrderPort, times(1)).sendOrder(any(Order.class));
        verify(idempotencyPort, times(1)).saveOrderCache(eq(idempotencyKey), any(Order.class));
    }

    @Test
    void shouldFindOrdersByStatusSuccessfully() {
        when(orderRepositoryPort.findOrderEntitiesByStatus(OrderStatus.PROCESSED.name()))
                .thenReturn(List.of(mockOrder));

        List<Order> orders = orderService.findOrderEntitiesByStatus("processed");

        assertNotNull(orders);
        assertEquals(1, orders.size());

        verify(orderRepositoryPort, times(1)).findOrderEntitiesByStatus(OrderStatus.PROCESSED.name());
    }
}
