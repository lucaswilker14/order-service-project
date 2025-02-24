package com.app.orderserviceproject.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheCleaner {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheCleaner.class);

    public RedisCacheCleaner(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Bean
    public CommandLineRunner clearRedisCache(RedisTemplate<String, Object> redisTemplate) {
        return args -> {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            LOGGER.info("Cleaning up Redis Cache");
        };
    }

}
