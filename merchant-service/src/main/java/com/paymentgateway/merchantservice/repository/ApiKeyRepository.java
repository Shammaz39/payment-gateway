package com.paymentgateway.merchantservice.repository;

import com.paymentgateway.merchantservice.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey,String> {
    ApiKey findByMerchantId(String merchantId);

    Optional<ApiKey> findByMerchantIdAndActiveTrue(String merchantId);
}
