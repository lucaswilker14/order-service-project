package com.app.orderserviceproject.port.output;

import com.app.orderserviceproject.domain.model.Order;

public interface KafkaOrderPort {

    boolean sendOrder(Order order) throws Exception;
}
