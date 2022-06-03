package com.finch.camunda.client.configurations.handler.exception;

public class NegocioException extends RuntimeException {

	public NegocioException() {}

	public NegocioException(String message) {super(message);}

	public NegocioException(String message, Throwable cause) {super(message, cause);}

}
