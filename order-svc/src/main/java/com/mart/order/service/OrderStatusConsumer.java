package com.mart.order.service;

import com.mart.common.dto.InventoryEvent;
import com.mart.common.dto.PaymentEvent;
import com.mart.order.model.Orders;
import com.mart.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderStatusConsumer {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private IdempotencyService idempotencyService;

    @KafkaListener(topics = "payment-success", groupId = "order-group", properties = {
            "spring.json.value.default.type=com.mart.common.dto.PaymentEvent"
    })
    public void handlePaymentSuccess(PaymentEvent event) {
        if (idempotencyService.isDuplicate(event.getEventId().toString())) {
            System.out.println("Duplicate event ignored: " + event.getEventId());
            return;
        }
        System.out.println("Completing order: " + event.getOrderId());

        ordersService.updateOrderStatus(event.getOrderId(),"COMPLETED");
    }

    @KafkaListener(topics = "inventory-failed", groupId = "order-group", properties = {
            "spring.json.value.default.type=com.mart.common.dto.InventoryEvent"
    })
    public void handleInventoryFailure(InventoryEvent event) {
        if (idempotencyService.isDuplicate(event.getEventId().toString())) {
            System.out.println("Duplicate event ignored: " + event.getEventId());
            return;
        }
        UUID orderId = event.getOrderId();
        System.out.println("Cancelling order inventory failed: " + orderId);

        ordersService.updateOrderStatus(event.getOrderId(),"CANCELLED");

    }

    @KafkaListener(topics = "payment-failed", groupId = "order-group", properties = {
            "spring.json.value.default.type=com.mart.common.dto.PaymentEvent"
    })
    public void handlePaymentFailure(PaymentEvent event) {
        if (idempotencyService.isDuplicate(event.getEventId().toString())) {
            System.out.println("Duplicate event ignored: " + event.getEventId());
            return;
        }
        UUID orderId = event.getOrderId();
        System.out.println("Cancelling order payment failed: " + orderId);

        ordersService.updateOrderStatus(event.getOrderId(),"CANCELLED");

    }
}
