package com.paymentgateway.merchantservice.repository;

import com.paymentgateway.merchantservice.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey,String> {
    ApiKey findByMerchantId(String merchantId);
}
