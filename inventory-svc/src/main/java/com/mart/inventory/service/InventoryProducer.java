package com.mart.inventory.service;

import com.mart.common.dto.InventoryEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventorySuccess(InventoryEvent event) {
        kafkaTemplate.send("inventory-success", event);
    }

    public void sendInventoryFailure(InventoryEvent event) {
        kafkaTemplate.send("inventory-failed", event);
    }
}
