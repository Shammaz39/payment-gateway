package com.paymentgateway.paymentprocessor.kafka;

import com.paymentgateway.commonlib.event.PaymentInitiatedEvent;
import com.paymentgateway.mockbank.dto.BankPaymentResponse;
import com.paymentgateway.paymentprocessor.entity.Transaction;
import com.paymentgateway.paymentprocessor.entity.TransactionStatus;
import com.paymentgateway.paymentprocessor.repository.TransactionRepository;
import com.paymentgateway.paymentprocessor.service.BankClient;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

    private final TransactionRepository transactionRepository;
    private final BankClient bankClient;

    public PaymentEventConsumer(
            TransactionRepository transactionRepository,
            BankClient bankClient
    ) {
        this.transactionRepository = transactionRepository;
        this.bankClient = bankClient;
    }

    @KafkaListener(
            topics = "payment.initiated",
            groupId = "payment-processing-group"
    )
    @Transactional
    public void consume(PaymentInitiatedEvent event) {

        System.out.println("================================");
        System.out.println("✅ Payment Event Received");
        System.out.println("Transaction ID: " + event.getTransactionId());

        Transaction tx = transactionRepository
                .findById(event.getTransactionId())
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        // Idempotency guard
        if (tx.getStatus() != TransactionStatus.PENDING) {
            System.out.println("⚠️ Transaction already processed");
            return;
        }

        try {
            System.out.println("➡️ Calling bank for authorization");

            BankPaymentResponse bankResponse =
                    bankClient.initiatePayment(tx);

            System.out.println("🏦 Bank initial response: "
                    + bankResponse.getStatus());


        } catch (Exception ex) {

            System.out.println("❌ Bank call failed: " + ex.getMessage());

            // Bank unreachable = FAILED
            tx.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(tx);

            // We swallow exception to avoid Kafka retry loop
        }

        System.out.println("================================");
    }
}
