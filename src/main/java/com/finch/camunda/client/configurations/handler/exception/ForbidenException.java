package com.finch.camunda.client.configurations.handler.exception;

public class ForbidenException extends  RuntimeException{

    public ForbidenException() { }

    public ForbidenException(final String message) {
        super(message);
    }

    public ForbidenException(final String message, final Throwable cause) {
        super(message, cause);
    }


}
