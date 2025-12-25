package com.paymentgateway.paymentservice.kafka;

import com.paymentgateway.commonlib.event.PaymentInitiatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {

    private static final String TOPIC = "payment.initiated";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentInitiated(PaymentInitiatedEvent event) {
        kafkaTemplate.send(TOPIC, String.valueOf(event.getTransactionId()), event);
    }
}
