package com.mart.payments.service;

import com.mart.common.dto.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentSuccess(PaymentEvent event) {
        kafkaTemplate.send("payment-success", event);
    }

    public void sendPaymentFailure(PaymentEvent event) {
        kafkaTemplate.send("payment-failed", event);
    }

}
