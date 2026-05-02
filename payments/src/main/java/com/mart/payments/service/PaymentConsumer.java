package com.mart.payments.service;

import com.mart.common.dto.InventoryEvent;
import com.mart.common.dto.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentConsumer {
    @Autowired
    private PaymentProducer producer;

    @Autowired
    private IdempotencyService idempotencyService;

    @KafkaListener(topics = "inventory-success", groupId = "payment-group")
    public void consume(InventoryEvent event) {
        if (idempotencyService.isDuplicate(event.getEventId().toString())) {
            System.out.println("Duplicate event ignored: " + event.getEventId());
            return;
        }
        System.out.println("Processing payment for order: " + event.getOrderId()); // Simulate payment
        boolean paymentSuccess = Math.random() > 0.2; // 80% success
        if (paymentSuccess) {
            System.out.println("Payment SUCCESS");
            producer.sendPaymentSuccess(new PaymentEvent(UUID.randomUUID(),event.getOrderId(), "SUCCESS", UUID.randomUUID().toString()));
        } else {
            System.out.println("Payment FAILED");
            producer.sendPaymentFailure(new PaymentEvent(UUID.randomUUID(),event.getOrderId(), "FAILED", null));
        }
    }
}
