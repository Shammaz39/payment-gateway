package com.paymentgateway.merchantservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "api_keys")
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String merchantId;

    @Column(nullable = false)
    private String hashedKey;   // We store only hashed version

    private LocalDateTime createdAt;

    private boolean active;
}
