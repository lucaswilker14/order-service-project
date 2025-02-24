package com.app.orderserviceproject.adapter.output.redis;

import com.app.orderserviceproject.adapter.input.dto.OrderDTO;
import com.app.orderserviceproject.adapter.mapper.OrderMapper;
import com.app.orderserviceproject.domain.model.Order;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class RedisIdempotencyAdapter implements RedisIdempotencyPort {

    private final CacheManager cacheManager;

    public RedisIdempotencyAdapter(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Order validadeIdempotencyKey(String idempotencyKey) {
        Cache cache = cacheManager.getCache("idempotency");
        Cache cache2 = cacheManager.getCache("Idempotency-Key");
        if (cache == null) return null;

        Cache.ValueWrapper cachedValue = cache.get(idempotencyKey);
        Cache.ValueWrapper cachedValue2 = cache2.get(idempotencyKey);
        if (cachedValue == null) return null;

        return OrderMapper.INSTANCE.toDomain((OrderDTO) cachedValue.get());

    }

    @Override
    public void saveOrderCache(String idempotencyKey, Order order) {
        Cache cache = cacheManager.getCache("idempotency");
        if (cache != null) {
            OrderDTO orderDTO = OrderMapper.INSTANCE.toDto(order);
            cache.put(idempotencyKey, orderDTO);
        }
    }
}
