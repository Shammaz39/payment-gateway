package com.paymentgateway.paymentservice.dto;

import lombok.Data;

@Data
public class MerchantLoginRequest {
    private String email;
    private String apiKey;

}
