package com.mart.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean isAllowed(String userId) {

        String key = "rate:" + userId;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(1));
        }

        return count <= 10; // 10 req/sec
    }
}