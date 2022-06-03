package com.finch.camunda.client.configurations.handler.exception;

public class GenericLoginErrorException extends RuntimeException {

    public GenericLoginErrorException() { }

    public GenericLoginErrorException(String message) {super(message);}

    public GenericLoginErrorException(String message, Throwable cause) { super(message, cause);}


}
