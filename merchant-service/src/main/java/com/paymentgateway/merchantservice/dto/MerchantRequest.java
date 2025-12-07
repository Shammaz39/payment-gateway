package com.paymentgateway.merchantservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MerchantRequest {
    @NotBlank
    private String name;

    @Email
    private String email;

    private String webhookUrl;
}
