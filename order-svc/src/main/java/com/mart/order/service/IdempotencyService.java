package com.mart.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String PREFIX = "event:";

    public boolean isDuplicate(String eventId) {

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(PREFIX + eventId, "processed", Duration.ofMinutes(10));

        return Boolean.FALSE.equals(success);
    }
}
