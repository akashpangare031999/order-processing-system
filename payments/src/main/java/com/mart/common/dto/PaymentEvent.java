package com.mart.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private UUID eventId;
    private UUID orderId;
    private String status; // SUCCESS / FAILED
    private String transactionId;
}
