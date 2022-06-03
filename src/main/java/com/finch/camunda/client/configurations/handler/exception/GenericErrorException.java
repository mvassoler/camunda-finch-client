package com.finch.camunda.client.configurations.handler.exception;

public class GenericErrorException extends RuntimeException {

    public GenericErrorException() { }

    public GenericErrorException(String message, Throwable cause) { super(message, cause); }

    public GenericErrorException(String message) { super(message); }

}
