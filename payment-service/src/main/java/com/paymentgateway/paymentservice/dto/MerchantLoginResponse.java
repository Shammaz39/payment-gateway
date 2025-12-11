package com.paymentgateway.paymentservice.dto;

import lombok.Data;

@Data
public class MerchantLoginResponse {
    private String token;
    private String message;

}
