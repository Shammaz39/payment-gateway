package com.paymentgateway.commonlib.event;


import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiatedEvent implements Serializable {

    private UUID transactionId;
    private String merchantId;
    private Double amount;
    private String currency;
}
