package com.finch.camunda.client.configurations.handler.exception;

public class UnAuthorizedeException extends  RuntimeException {

    public UnAuthorizedeException() {}

    public UnAuthorizedeException(String message) {
        super(message);
    }

    public UnAuthorizedeException(String message, Throwable cause) {
        super(message, cause);
    }

}
