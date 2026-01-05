package com.paymentgateway.commonlib.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessedEvent {

    private UUID transactionId;
    private String merchantId;
    private String status; // SUCCESS / FAILED
}

