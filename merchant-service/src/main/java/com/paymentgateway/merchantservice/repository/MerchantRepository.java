package com.paymentgateway.merchantservice.repository;

import com.paymentgateway.merchantservice.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  MerchantRepository extends JpaRepository<Merchant, String> {
    boolean existsByEmail(String email);
    Merchant findByEmail(String email);
}
