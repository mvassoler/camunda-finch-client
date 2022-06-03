package com.finch.camunda.client.configurations.handler.exception;

/**
 * @author Thiago Fogar
 * @since
 */
public class InvalidIndiceEconomicoException extends RuntimeException {

    public InvalidIndiceEconomicoException() { }

    public InvalidIndiceEconomicoException(String message) {
        super(message);
    }

    public InvalidIndiceEconomicoException(String message, Throwable cause) {
        super(message, cause);
    }

}
