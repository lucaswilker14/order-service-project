package com.app.orderserviceproject.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.properties.topic}")
    private String orderTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", servers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic myTopic() {
        return new NewTopic(orderTopic, 1, (short) 1);
    }
}
