package com.app.orderserviceproject.adapter.output.redis;

import com.app.orderserviceproject.domain.model.Order;

public interface RedisIdempotencyPort {

    Order validadeIdempotencyKey(String idempotencyKey);

    void saveOrderCache(String idempotencyKey, Order order);
}
