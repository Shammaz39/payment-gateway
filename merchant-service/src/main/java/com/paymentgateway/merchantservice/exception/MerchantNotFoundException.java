package com.paymentgateway.merchantservice.exception;

public class MerchantNotFoundException extends RuntimeException{
    public MerchantNotFoundException(String message){
        super(message);
    }
}
