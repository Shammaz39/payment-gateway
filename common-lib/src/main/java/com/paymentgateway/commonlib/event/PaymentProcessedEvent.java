package com.paymentgateway.commonlib.event;


import java.util.UUID;

public class PaymentProcessedEvent {

    private Long transactionId;
    private String status; // SUCCESS / FAILED

    public PaymentProcessedEvent() {}

    public PaymentProcessedEvent(Long transactionId, String status) {
        this.transactionId = transactionId;
        this.status = status;
    }

    public PaymentProcessedEvent(UUID id, String name) {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

