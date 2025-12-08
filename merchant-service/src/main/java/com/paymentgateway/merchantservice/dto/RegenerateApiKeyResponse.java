package com.paymentgateway.merchantservice.dto;

import lombok.Data;

@Data
public class RegenerateApiKeyResponse {
    private String message;
    private String newApiKey;
}
