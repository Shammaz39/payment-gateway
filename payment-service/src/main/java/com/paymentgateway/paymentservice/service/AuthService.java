package com.paymentgateway.paymentservice.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String authenticateMerchant(String email, String apiKey);
}

