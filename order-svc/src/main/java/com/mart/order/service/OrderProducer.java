package com.mart.order.service;

import com.mart.common.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "order-created";

    public void sendOrderEvent(OrderEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }

}
