package com.paymentgateway.notificationservice.service;


import com.paymentgateway.commonlib.event.PaymentProcessedEvent;
import com.paymentgateway.notificationservice.client.MerchantClient;
//import com.paymentgateway.notificationservice.entity.PaymentLog;
//import com.paymentgateway.notificationservice.repository.PaymentLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private final MerchantClient merchantClient;
    private final RestTemplate restTemplate = new RestTemplate();
//    private final PaymentLogRepository paymentLogRepository;

    public NotificationService(MerchantClient merchantClient
//            , PaymentLogRepository paymentLogRepository
    ) {
        this.merchantClient = merchantClient;
//        this.paymentLogRepository = paymentLogRepository;
    }

    public void handle(PaymentProcessedEvent event) {

        // 1️⃣ Log final status (async)
        System.out.println(
                "📝 Payment " + event.getStatus()
                        + " for TX " + event.getTransactionId()
        );

//        // 1️⃣ Log final payment status
//        paymentLogRepository.save(
//                PaymentLog.builder()
//                        .transactionId(event.getTransactionId())
//                        .message("Payment " + event.getStatus())
//                        .build()
//        );

        // 2️⃣ Fetch merchant webhook URL
        String webhookUrl =
                merchantClient.fetchWebhookUrl(event.getMerchantId());

        // 3️⃣ Send notification (mock webhook)
        try {
            restTemplate.postForObject(
                    webhookUrl,
                    event,
                    Void.class
            );
            System.out.println("✅ Notification sent");
        } catch (Exception ex) {
            System.out.println("❌ Notification failed");
        }
    }
}
