package com.finch.camunda.client.configurations.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.finch.camunda.client.configurations.constants.TitleValidationConstants;
import com.finch.camunda.client.configurations.handler.exception.BadGatewayException;
import com.finch.camunda.client.configurations.handler.exception.BadRequestException;
import com.finch.camunda.client.configurations.handler.exception.ConflictException;
import com.finch.camunda.client.configurations.handler.exception.DataHibernacaoInvalidaException;
import com.finch.camunda.client.configurations.handler.exception.EntityNotFoundException;
import com.finch.camunda.client.configurations.handler.exception.FileAttachmentException;
import com.finch.camunda.client.configurations.handler.exception.ForbidenException;
import com.finch.camunda.client.configurations.handler.exception.GatewayTimeoutException;
import com.finch.camunda.client.configurations.handler.exception.GenericErrorException;
import com.finch.camunda.client.configurations.handler.exception.GoneException;
import com.finch.camunda.client.configurations.handler.exception.IdConflictException;
import com.finch.camunda.client.configurations.handler.exception.InternalServerErrorException;
import com.finch.camunda.client.configurations.handler.exception.NegocioException;
import com.finch.camunda.client.configurations.handler.exception.PayloadTooLargeException;
import com.finch.camunda.client.configurations.handler.exception.PaymentRequiredException;
import com.finch.camunda.client.configurations.handler.exception.UnAuthorizedeException;
import com.finch.camunda.client.configurations.handler.exception.UnauthorizedAccessException;
import com.finch.camunda.client.domains.dtos.ErrorDetailsDTO;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.GATEWAY_TIMEOUT;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.PAYMENT_REQUIRED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@ControllerAdvice
@Log4j2
public class GlobalEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
            = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    public GlobalEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handler para tratar BadRequestException, lançada pelos serviços.
     *
     * @param ex      a exception
     * @return ResponseEntity como 400 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequestException(final BadRequestException ex, WebRequest request) {
        log.info("M=BadRequestException", ex);
        HttpStatus status = BAD_REQUEST;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar EntityNotFoundException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 404 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFoundException(final EntityNotFoundException ex, WebRequest request) {
        log.info("M=EntityNotFoundException", ex);
        HttpStatus status = NOT_FOUND;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.ENTIDADE_NAO_ENCONTRADA, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar UnAuthorizedeException, lançada pelos serviços.
     *
     * @param ex      a exception
     * @return ResponseEntity como 401 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(UnAuthorizedeException.class)
    public ResponseEntity<Object> unAuthorizedeException(final UnAuthorizedeException ex, WebRequest request) {
        log.info("M=UnAuthorizedeException", ex);
        HttpStatus status = UNAUTHORIZED;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.SEM_AUTORIZACAO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar PaymentRequiredException, lançada pelos serviços.
     *
     * @param ex      a exception
     * @return ResponseEntity como 402 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(PaymentRequiredException.class)
    public ResponseEntity<Object> paymentRequiredException(final PaymentRequiredException ex, WebRequest request) {
        log.info("M=PaymentRequiredException", ex);
        HttpStatus status = PAYMENT_REQUIRED;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar ForbidenException, lançada pelos serviços.
     *
     * @param ex      a exception
     * @return ResponseEntity como 403 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(ForbidenException.class)
    public ResponseEntity<Object> forbidenException(final ForbidenException ex, WebRequest request) {
        log.info("M=ForbidenException", ex);
        HttpStatus status = FORBIDDEN;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.SEM_AUTORIZACAO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar ConflictException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 409 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> conflictException(final ConflictException ex, WebRequest request) {
        log.info("M=ConflictException", ex);
        HttpStatus status = CONFLICT;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar GoneException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 410 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(GoneException.class)
    public ResponseEntity<Object> goneException(final GoneException ex, WebRequest request) {
        log.info("M=GoneException", ex);
        HttpStatus status = GONE;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }


    /**
     * Handler para tratar PayloadTooLargeException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 413 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(PayloadTooLargeException.class)
    public ResponseEntity<Object> payloadTooLargeException(final PayloadTooLargeException ex, WebRequest request) {
        log.info("M=PayloadTooLargeException", ex);
        HttpStatus status = PAYLOAD_TOO_LARGE;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar InternalServerErrorException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 500 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> internalServerErrorException(final InternalServerErrorException ex, WebRequest request) {
        log.info("M=InternalServerErrorException", ex);
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.SERVER_ERROR, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar BadGatewayException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 502 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(BadGatewayException.class)
    public ResponseEntity<Object> badGatewayException(final BadGatewayException ex, WebRequest request) {
        log.info("M=BadGatewayException", ex);
        HttpStatus status = BAD_GATEWAY;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.SERVER_ERROR, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handler para tratar GatewayTimeoutException, lançada pelo CrudService se o id passado no find não existe.
     *
     * @param ex      a exception
     * @return ResponseEntity como 504 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(GatewayTimeoutException.class)
    public ResponseEntity<Object> gatewayTimeoutException(final GatewayTimeoutException ex,WebRequest request) {
        log.info("M=GatewayTimeoutException", ex);
        ErrorDetailsDTO errorDetailsDTO = createProblemBuilder(GATEWAY_TIMEOUT, TitleValidationConstants.TIME_OUT, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, errorDetailsDTO, new HttpHeaders(), GATEWAY_TIMEOUT, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
        log.info("M=NegocioException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(BAD_REQUEST, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(DataHibernacaoInvalidaException.class)
    protected ResponseEntity<Object> handleDataHibernacaoInvalida(DataHibernacaoInvalidaException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("M=DataHibernacaoInvalidaException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleValidationConstants.DATA_HIBERNACAO_INVALIDA, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(GenericErrorException.class)
    protected ResponseEntity<Object> handleGenericError(GenericErrorException ex,  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("M=GenericErrorException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleValidationConstants.ERRO_GENERICO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(IdConflictException.class)
    protected ResponseEntity<Object> handleIdConflict(IdConflictException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("M=IdConflictException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleValidationConstants.ID_CONFLITO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    protected ResponseEntity<Object> handleUnauthorized(UnauthorizedAccessException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("M=UnauthorizedAccessException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleValidationConstants.SEM_AUTORIZACAO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handler para tratar ForbidenException, lançada pelos serviços.
     *
     * @param ex      a exception
     * @return ResponseEntity como 403 e contendo mensagem no corpo do response.
     */
    @ExceptionHandler(FileAttachmentException.class)
    public ResponseEntity<Object> forbidenException(final FileAttachmentException ex, WebRequest request) {
        log.info("M=FileAttachmentException", ex);
        HttpStatus status = BAD_REQUEST;
        ErrorDetailsDTO error = createProblemBuilder(status, TitleValidationConstants.SEM_AUTORIZACAO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request){
        log.info("M=handleBindException", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.info("M=handleMethodArgumentNotValid", ex.getMessage());
        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request){
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormat(invalidFormatException, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBinding(propertyBindingException, headers, status, request);
        }
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
        ErrorDetailsDTO problem = createOtherProblemBuilder(status, TitleValidationConstants.MENSAGEM_INCOMPREENSIVEL, detail,
                ((ServletWebRequest)request).getRequest().getRequestURL().toString())
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,             final WebRequest request) {
        log.info("M=handleMissingServletRequestParameter", ex);
        ErrorDetailsDTO problem = createProblemBuilder(status, TitleValidationConstants.ERRO_NEGOCIO, ex.getMessage(),
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request, BindingResult bindingResult) {
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<ErrorDetailsDTO.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError fieldError) {
                        name = fieldError.getField();
                    }

                    return ErrorDetailsDTO.Object.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        ErrorDetailsDTO problem = createOtherProblemBuilder(status, TitleValidationConstants.DADOS_INVALIDOS, detail, ((ServletWebRequest)request).getRequest().getRequestURL().toString())
                .userMessage(detail)
                .objects(problemObjects)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = ErrorDetailsDTO.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        } else if (body instanceof String bodyStr) {
            body = ErrorDetailsDTO.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(bodyStr)
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ErrorDetailsDTO createProblemBuilder(HttpStatus status,  String title, String detail, String contextPath) {
        return ErrorDetailsDTO.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(contextPath)
                .title(title)
                .detail(detail).build();
    }

    private ErrorDetailsDTO.ErrorDetailsDTOBuilder createOtherProblemBuilder(HttpStatus status, String title, String detail, String contextPath) {
        return ErrorDetailsDTO.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .type(contextPath)
                .title(title)
                .detail(detail);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        ErrorDetailsDTO problem = createOtherProblemBuilder(status, TitleValidationConstants.MENSAGEM_INCOMPREENSIVEL, detail,
                ((ServletWebRequest)request).getRequest().getRequestURL().toString())
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        String detail = String.format("A propriedade '%s' não existe. "
                + "Corrija ou remova essa propriedade e tente novamente.", path);
        ErrorDetailsDTO problem = createOtherProblemBuilder(status, TitleValidationConstants.MENSAGEM_INCOMPREENSIVEL, detail,
                ((ServletWebRequest)request).getRequest().getRequestURL().toString())
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }
}