package com.paymentgateway.merchantservice.dto;

import lombok.Data;

@Data
public class MerchantValidateRequest {
    private String email;
    private String apiKey;
}
