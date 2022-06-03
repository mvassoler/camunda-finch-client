package com.finch.camunda.client.configurations.handler.exception;

public class JwtException extends RuntimeException {

    public JwtException() {}

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }

}
