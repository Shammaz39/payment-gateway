package com.paymentgateway.paymentprocessor.dto;

import lombok.Data;

@Data
public class BankCallbackRequest {

    private String transactionId;
    private String status; // SUCCESS / FAILED
}
