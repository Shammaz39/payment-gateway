package com.paymentgateway.paymentservice.repository;

import com.paymentgateway.paymentservice.entity.Transaction;
import com.paymentgateway.paymentservice.enums.TransactionStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByMerchantIdOrderByCreatedAtDesc(String merchantId);

    List<Transaction> findByMerchantIdAndStatusOrderByCreatedAtDesc(
            String merchantId,
            TransactionStatus status
    );

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.merchantId = :merchantId
          AND (:status IS NULL OR t.status = :status)
          AND (:minAmount IS NULL OR t.amount >= :minAmount)
          AND (:maxAmount IS NULL OR t.amount <= :maxAmount)
        ORDER BY t.createdAt DESC
    """)
    List<Transaction> filterWithoutDates(
            String merchantId,
            TransactionStatus status,
            Double minAmount,
            Double maxAmount
    );

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.merchantId = :merchantId
          AND t.createdAt BETWEEN :fromDate AND :toDate
        ORDER BY t.createdAt DESC
    """)
    List<Transaction> filterWithDates(
            String merchantId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );
}

