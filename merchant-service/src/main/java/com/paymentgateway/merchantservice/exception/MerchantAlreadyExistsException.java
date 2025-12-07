package com.paymentgateway.merchantservice.exception;

public class MerchantAlreadyExistsException extends RuntimeException{
    public MerchantAlreadyExistsException(String message) {
        super(message);
    }
}
