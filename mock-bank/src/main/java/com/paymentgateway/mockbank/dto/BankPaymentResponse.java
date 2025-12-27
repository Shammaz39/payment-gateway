package com.paymentgateway.mockbank.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BankPaymentResponse {
    private String transactionId;
    private String status;
}
