package com.mart.inventory.service;

import com.mart.common.dto.InventoryEvent;
import com.mart.common.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryConsumer {
    @Autowired
    private InventoryProducer producer;

    @Autowired
    private  IdempotencyService idempotencyService;

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void consume(OrderEvent event) {
        if (idempotencyService.isDuplicate(event.getEventId().toString())) {
            System.out.println("Duplicate event ignored: " + event.getEventId());
            return;
        }
        System.out.println("Processing order: " + event.getOrderId()); // Simulate inventory check
        boolean inStock = Math.random() > 0.3; // 70% success
        if (inStock) {
            System.out.println("Inventory SUCCESS");
            producer.sendInventorySuccess(new InventoryEvent(UUID.randomUUID(),event.getOrderId(), "SUCCESS", null));
        } else {
            System.out.println("Inventory FAILED");
            producer.sendInventoryFailure(new InventoryEvent(UUID.randomUUID(),event.getOrderId(), "FAILED", "Out of stock"));
        }
    }
}
