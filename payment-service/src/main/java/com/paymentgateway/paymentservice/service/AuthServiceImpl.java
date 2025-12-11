package com.paymentgateway.paymentservice.service;

import com.paymentgateway.paymentservice.client.MerchantClient;
import com.paymentgateway.paymentservice.exception.InValidException;
import com.paymentgateway.paymentservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private MerchantClient merchantClient;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public String authenticateMerchant(String email, String apiKey) {
        // 1. Call merchant-service to validate
        boolean isValid = merchantClient.validateMerchant(email, apiKey);

        if (!isValid) {
            throw new InValidException("Invalid email or API key");
        }

        // 2. If valid → Generate JWT
        // later we will fetch merchantId instead of email
        String token = jwtUtil.generateToken(email, email);

        return token;
    }
}
