package com.paymentgateway.paymentprocessor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    private UUID id;

    private String merchantId;
    private Double amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
