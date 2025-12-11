package com.paymentgateway.merchantservice.service;

import com.paymentgateway.merchantservice.dto.MerchantRequest;
import com.paymentgateway.merchantservice.entity.ApiKey;
import com.paymentgateway.merchantservice.entity.Merchant;
import com.paymentgateway.merchantservice.exception.MerchantAlreadyExistsException;
import com.paymentgateway.merchantservice.exception.MerchantNotFoundException;
import com.paymentgateway.merchantservice.repository.ApiKeyRepository;
import com.paymentgateway.merchantservice.repository.MerchantRepository;
import com.paymentgateway.merchantservice.util.ApiKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    @Autowired
    private final ApiKeyRepository apiKeyRepository;

    public MerchantService(MerchantRepository merchantRepository, ApiKeyRepository apiKeyRepository) {
        this.merchantRepository = merchantRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }


    public Merchant registerMerchant(MerchantRequest request) {

        if (merchantRepository.existsByEmail(request.getEmail())) {
            throw new MerchantAlreadyExistsException("Merchant already exists with this email");
        }

        // save merchant
        Merchant merchant = new Merchant();
        merchant.setName(request.getName());
        merchant.setEmail(request.getEmail());
        merchant.setWebhookUrl(request.getWebhookUrl());
        merchant.setStatus("ACTIVE");
        merchant.setCreatedAt(LocalDateTime.now());
        Merchant saved = merchantRepository.save(merchant);

        // generate API key
        String rawApiKey = ApiKeyUtil.generateApiKey();
        String hashed = BCrypt.hashpw(rawApiKey, BCrypt.gensalt());

        ApiKey apiKey = new ApiKey();
        apiKey.setMerchantId(saved.getId());
        apiKey.setHashedKey(hashed);
        apiKey.setActive(true);
        apiKey.setCreatedAt(LocalDateTime.now());
        apiKeyRepository.save(apiKey);

        // return raw key to merchant ONCE
        saved.setApiKey(rawApiKey); // temporary field for response

        return saved;
    }


    public String regenerateApiKey(String merchantId) {

        // 1. Check merchant exists
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant not found"));

        // 2. Deactivate old key (if any)
        apiKeyRepository.findByMerchantIdAndActiveTrue(merchant.getId())
                .ifPresent(oldKey -> {
                    oldKey.setActive(false);
                    apiKeyRepository.save(oldKey);
                });

        // 3. Generate new raw key
        String newRawKey = ApiKeyUtil.generateApiKey();
        String newHashed = BCrypt.hashpw(newRawKey, BCrypt.gensalt());

        // 4. Save new key as active
        ApiKey newKey = new ApiKey();
        newKey.setMerchantId(merchant.getId());
        newKey.setHashedKey(newHashed);
        newKey.setActive(true);
        newKey.setCreatedAt(LocalDateTime.now());

        apiKeyRepository.save(newKey);

        // 5. Return raw key ONCE
        return newRawKey;
    }


    public boolean validateMerchant(String email, String rawApiKey) {

        // 1. Find merchant by email
        Merchant merchant = merchantRepository.findByEmail(email);
        if (merchant == null) {
            return false;
        }

        // 2. Get merchant's active API key
        ApiKey key = apiKeyRepository.findByMerchantIdAndActiveTrue(merchant.getId())
                .orElse(null);

        if (key == null) {
            return false;
        }

        // 3. Compare raw key with stored hashed key
        return BCrypt.checkpw(rawApiKey, key.getHashedKey());
    }



}
