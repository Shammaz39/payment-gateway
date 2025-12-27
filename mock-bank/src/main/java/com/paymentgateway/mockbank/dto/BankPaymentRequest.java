package com.paymentgateway.mockbank.dto;

import lombok.Data;


@Data
public class BankPaymentRequest {
    private String transactionId;
    private Double amount;
    private String callbackUrl;
}
