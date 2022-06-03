package com.finch.camunda.client.configurations.handler.exception;

/**
 * Exceção para métodos de integração com Gracco.
 *
 * @author Marcelo Aguiar
 * @since 2.1
 */
public class IntegrationException extends RuntimeException {

    public IntegrationException() { }

    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

}
