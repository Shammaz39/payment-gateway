package com.paymentgateway.paymentprocessor.kafka;

import com.paymentgateway.commonlib.event.PaymentInitiatedEvent;
import com.paymentgateway.paymentprocessor.entity.Transaction;
import com.paymentgateway.paymentprocessor.entity.TransactionStatus;
import com.paymentgateway.paymentprocessor.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {

    private final TransactionRepository transactionRepository;

    public PaymentEventConsumer(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @KafkaListener(
            topics = "payment.initiated",
            groupId = "payment-group"
    )
    public void consume(PaymentInitiatedEvent event) {

        System.out.println("================================");
        System.out.println("✅ Payment Event Received by Group Id payment-group");
        System.out.println("Transaction ID: " + event.getTransactionId());

        Transaction tx = transactionRepository
                .findById(event.getTransactionId())
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        if (tx.getStatus() != TransactionStatus.PENDING) {
            System.out.println("⚠️ Transaction already processed");
            return;
        }

        // Day 10 ends here
        // Day 11 → call bank
        System.out.println("➡️ Ready to process payment");
        System.out.println("================================");
    }
}
