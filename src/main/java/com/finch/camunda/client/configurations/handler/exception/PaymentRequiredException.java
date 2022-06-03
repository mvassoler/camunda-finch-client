package com.finch.camunda.client.configurations.handler.exception;

public class PaymentRequiredException extends RuntimeException {

    public PaymentRequiredException() { }

    public PaymentRequiredException(String message) {
        super(message);
    }

    public PaymentRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

}
