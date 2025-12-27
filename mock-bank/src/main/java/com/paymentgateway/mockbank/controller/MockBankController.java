package com.paymentgateway.mockbank.controller;


import com.paymentgateway.mockbank.dto.BankPaymentRequest;
import com.paymentgateway.mockbank.dto.BankPaymentResponse;
import com.paymentgateway.mockbank.service.BankProcessingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class MockBankController {

    private final BankProcessingService bankProcessingService;

    public MockBankController(BankProcessingService bankProcessingService) {
        this.bankProcessingService = bankProcessingService;
    }

    @PostMapping("/pay")
    public BankPaymentResponse pay(@RequestBody BankPaymentRequest request) {
        return bankProcessingService.process(request);
    }
}
