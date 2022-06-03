package com.finch.camunda.client.configurations.handler.exception;

/**
 * Exceção entidade não encontrada.
 *
 * @author Rodolpho Couto
 * @since 2.1
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException () { }

    public EntityNotFoundException (final String message) {
        super(message);
    }

    public EntityNotFoundException (final String message, final Throwable cause) {
        super(message, cause);
    }
}
