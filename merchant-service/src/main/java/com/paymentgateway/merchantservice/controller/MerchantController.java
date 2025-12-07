package com.paymentgateway.merchantservice.controller;

import com.paymentgateway.merchantservice.dto.MerchantRequest;
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
    public Merchant register(@Valid @RequestBody MerchantRequest request) {
        return merchantService.registerMerchant(request);
    }

    @GetMapping("/all")
    public List<Merchant> register() {
        return merchantService.getAllMerchants();
    }
}
