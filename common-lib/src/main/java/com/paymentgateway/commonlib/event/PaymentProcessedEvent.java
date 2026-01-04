package com.paymentgateway.commonlib.event;


import java.util.UUID;

public class PaymentProcessedEvent {

    private UUID transactionId;
    private String merchantId;
    private String status; // SUCCESS / FAILED

    public PaymentProcessedEvent() {}

    public PaymentProcessedEvent(
            UUID transactionId,
            String merchantId,
            String status
    ) {
        this.transactionId = transactionId;
        this.merchantId = merchantId;
        this.status = status;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getStatus() {
        return status;
    }
}

