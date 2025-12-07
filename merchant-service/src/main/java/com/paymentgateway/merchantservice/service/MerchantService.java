package com.paymentgateway.merchantservice.service;

import com.paymentgateway.merchantservice.dto.MerchantRequest;
import com.paymentgateway.merchantservice.entity.Merchant;
import com.paymentgateway.merchantservice.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    public Merchant registerMerchant(MerchantRequest request) {
        Merchant merchant = new Merchant();
        merchant.setName(request.getName());
        merchant.setEmail(request.getEmail());
        merchant.setWebhookUrl(request.getWebhookUrl());
        merchant.setStatus("ACTIVE");
        merchant.setCreatedAt(LocalDateTime.now());

        return merchantRepository.save(merchant);
    }

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

}
