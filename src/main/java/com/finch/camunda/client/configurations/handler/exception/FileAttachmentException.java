package com.finch.camunda.client.configurations.handler.exception;

/**
 * Exceção para erro ao anexar arquivo ao e-mail
 *
 * @author Felipi Berdun
 * @since 2.1
 */
public class FileAttachmentException extends RuntimeException {

    public FileAttachmentException() { }

    public FileAttachmentException(final String message) {
        super(message);
    }

    public FileAttachmentException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
