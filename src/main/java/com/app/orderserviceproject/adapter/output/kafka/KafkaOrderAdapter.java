package com.app.orderserviceproject.adapter.output.kafka;

import com.app.orderserviceproject.adapter.input.dto.OrderDTO;
import com.app.orderserviceproject.adapter.mapper.OrderMapper;
import com.app.orderserviceproject.domain.model.Order;
import com.app.orderserviceproject.domain.service.OrderService;
import com.app.orderserviceproject.port.output.KafkaOrderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaOrderAdapter implements KafkaOrderPort {

    @Autowired
    private KafkaTemplate<String, OrderDTO> kafkaTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Value("${spring.kafka.producer.properties.topic}")
    private String orderTopic;

    @Override
    public boolean sendOrder(Order order) throws Exception {
        
        try {
            OrderDTO orderDTO = OrderMapper.INSTANCE.toDto(order);

            LOGGER.error("Send Kafka Order: {}", orderDTO);
            CompletableFuture<SendResult<String, OrderDTO>> future = kafkaTemplate.send(orderTopic, orderDTO);

            future.thenAcceptAsync(success -> LOGGER.info("Order sent successfully: {}", success.getProducerRecord().value()))
                    .exceptionallyAsync(failure -> {
                        LOGGER.error("Failed to send order to Kafka", failure);
                        throw new RuntimeException("Failed to send order to Kafka", failure);
                    });

            return true;

        }catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
