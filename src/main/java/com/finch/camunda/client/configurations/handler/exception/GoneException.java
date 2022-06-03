package com.finch.camunda.client.configurations.handler.exception;

public class GoneException extends RuntimeException{

    public GoneException() {  }

    public GoneException(String message) {
        super(message);
    }

    public GoneException(String message, Throwable cause) {
        super(message, cause);
    }

}
