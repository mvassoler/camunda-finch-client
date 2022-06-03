package com.finch.camunda.client.configurations.handler.exception;

public class BadGatewayException extends RuntimeException {

    public BadGatewayException() { }

    public BadGatewayException(final String message) {
        super(message);
    }

    public BadGatewayException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
