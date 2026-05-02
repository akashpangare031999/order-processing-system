package com.mart.order.controller;

import com.mart.common.dto.OrderEvent;
import com.mart.order.model.Orders;
import com.mart.order.repository.OrderRepository;
import com.mart.order.service.OrderProducer;
import com.mart.order.service.OrdersService;
import com.mart.order.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RateLimiterService rateLimiterService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Orders order) {
        if (!rateLimiterService.isAllowed(order.getUserId())) {
            return ResponseEntity.status(429).body("Too many requests");
        }
        Orders saved = ordersService.createOrders(order);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Orders> getOrders(@RequestParam UUID orderId) {
        return ResponseEntity.ok(ordersService.getOrder(orderId));
    }
}
