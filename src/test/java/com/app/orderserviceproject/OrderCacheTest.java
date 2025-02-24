package com.app.orderserviceproject;

import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.port.input.OrderServicePort;
import com.app.orderserviceproject.port.output.OrderRepositoryPort;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderCacheTest {

    @Autowired
    private OrderRepositoryPort orderRepositoryPort;

    @Autowired
    private CacheManager cacheManager;

    @Mock
    private OrderServicePort orderServicePort; // Mock do serviço para evitar chamadas reais

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setup() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @AfterEach
    void down() {
        Objects.requireNonNull(cacheManager.getCache("orders")).clear();
    }

    @Test
    public void testOrderCaching() {
        String orderId = "67b68dea2ad7463b6c7e7e77";


        // Primeira chamada -> busca no banco e coloca no cache
        var firstCall = orderRepositoryPort.getOrder(orderId);

        // Segunda chamada -> deveria vir do cache
        var secondCall = orderRepositoryPort.getOrder(orderId);

        var Call = orderRepositoryPort.getOrder(orderId);
        var Call2 = orderRepositoryPort.getOrder(orderId);


        cacheManager.getCache("orders").clear();


        var Call3 = orderRepositoryPort.getOrder(orderId);
        var Call4 = orderRepositoryPort.getOrder(orderId);

        // Confirma que os dois objetos são iguais (cache funcionando | equals e hashCode)
        assertThat(firstCall).isEqualTo(secondCall);
        assertThat(Call).isEqualTo(Call2);
        assertThat(Call3).isEqualTo(Call4);

        // Confirma que a chave foi armazenada no cache
        Assertions.assertNotNull(Objects.requireNonNull(cacheManager.getCache("orders")).get(orderId));

    }
}
