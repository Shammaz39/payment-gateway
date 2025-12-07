package com.paymentgateway.merchantservice.controller;

import com.paymentgateway.merchantservice.dto.MerchantRequest;
import com.paymentgateway.merchantservice.dto.MerchantResponse;
import com.paymentgateway.merchantservice.entity.Merchant;
import com.paymentgateway.merchantservice.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/register")
    public MerchantResponse register(@Valid @RequestBody MerchantRequest request) {
        Merchant saved = merchantService.registerMerchant(request);

        MerchantResponse response = new MerchantResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setApiKey(saved.getApiKey()); // raw key

        return response;
    }

    @GetMapping("/all")
    public List<Merchant> register() {
        return merchantService.getAllMerchants();
    }
}
