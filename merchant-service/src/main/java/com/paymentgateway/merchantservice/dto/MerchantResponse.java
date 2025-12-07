package com.paymentgateway.merchantservice.dto;

import lombok.Data;

@Data
public class MerchantResponse {
    private String id;
    private String name;
    private String email;
    private String apiKey;  // Only shown once
}
