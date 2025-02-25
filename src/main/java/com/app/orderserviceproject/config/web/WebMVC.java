package com.app.orderserviceproject.config.web;

import com.app.orderserviceproject.adapter.output.redis.RedisPort;
import com.app.orderserviceproject.domain.service.OrderService;
import com.app.orderserviceproject.port.input.OrderServicePort;
import com.app.orderserviceproject.port.output.KafkaOrderPort;
import com.app.orderserviceproject.port.output.OrderRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMVC {

    @Bean
    public OrderServicePort orderService(OrderRepositoryPort orderRepositoryPort, KafkaOrderPort kafkaOrderPort,
                                         RedisPort idempotencyPort) {
        return new OrderService(orderRepositoryPort, kafkaOrderPort, idempotencyPort);
    }

}
