package com.paymentgateway.merchantservice.controller;

import com.paymentgateway.merchantservice.dto.MerchantRequest;
import com.paymentgateway.merchantservice.dto.MerchantResponse;
import com.paymentgateway.merchantservice.dto.MerchantValidateRequest;
import com.paymentgateway.merchantservice.dto.RegenerateApiKeyResponse;
import com.paymentgateway.merchantservice.entity.Merchant;
import com.paymentgateway.merchantservice.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/register")
    public MerchantResponse register(@Valid @RequestBody MerchantRequest request) {
        Merchant saved = merchantService.registerMerchant(request);

        MerchantResponse response = new MerchantResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setEmail(saved.getEmail());
        response.setApiKey(saved.getApiKey()); // raw key

        return response;
    }

    @GetMapping("/all")
    public List<Merchant> register() {
        return merchantService.getAllMerchants();
    }

    @PostMapping("/{merchantId}/regenerate-api-key")
    @Operation(
            summary = "Regenerate merchant API key",
            description = "Generates a new API key for the given merchant and returns it in the response."
    )
    public RegenerateApiKeyResponse regenerateApiKey(@Parameter(
                                                        description = "Unique merchant ID",
                                                        example = "mrc_12345"
                                                        )
                                                       @PathVariable("merchantId") String merchantId
    ) {
        String newKey = merchantService.regenerateApiKey(merchantId);

        RegenerateApiKeyResponse response = new RegenerateApiKeyResponse();
        response.setMessage("API key regenerated successfully");
        response.setNewApiKey(newKey);

        return response;
    }



    @PostMapping("/validate")
    public boolean validateMerchant(@RequestBody MerchantValidateRequest req) {
        return merchantService.validateMerchant(req.getEmail(), req.getApiKey());
    }


}
