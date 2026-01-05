package com.paymentgateway.paymentprocessor.repository;


import com.paymentgateway.paymentprocessor.entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLogRepository
        extends JpaRepository<PaymentLog, Long> {
}
