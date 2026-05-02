package com.mart.order.service;

import com.mart.common.dto.OrderEvent;
import com.mart.order.model.Orders;
import com.mart.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.security.PublicKey;
import java.time.Duration;
import java.util.UUID;

@Service
public class OrdersService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private OrderRepository orderRepository;

    public Orders getOrder(UUID orderId) {

        String key = "order:" + orderId;

        // 1. Try cache
        String cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            return objectMapper.readValue(cached, Orders.class);
        }

        // 2. DB fallback
        Orders order = orderRepository.findById(orderId).orElseThrow();

        // 3. Cache it
        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(order), Duration.ofMinutes(5));

        return order;
    }

    public Orders createOrders(Orders order) {
        order.setStatus("CREATED");
        Orders saved = orderRepository.save(order);
        OrderEvent event = new OrderEvent(UUID.randomUUID(), saved.getId(), saved.getUserId(), saved.getStatus());
        orderProducer.sendOrderEvent(event);
        return saved;
    }

    public void updateOrderStatus(UUID orderId, String status) {

        Orders order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);

        redisTemplate.delete("order:" + orderId);
    }
}
