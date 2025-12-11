package com.paymentgateway.paymentservice.controller;

import com.paymentgateway.paymentservice.dto.MerchantLoginRequest;
import com.paymentgateway.paymentservice.dto.MerchantLoginResponse;
import com.paymentgateway.paymentservice.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public MerchantLoginResponse login(@RequestBody MerchantLoginRequest request) {

        String token = authService.authenticateMerchant(
                request.getEmail(),
                request.getApiKey()
        );

        MerchantLoginResponse res = new MerchantLoginResponse();
        res.setMessage("Login successful");
        res.setToken(token);

        return res;
    }

    @GetMapping("/check")
    public String login() {
        return "Ok MANH";
    }


    }
