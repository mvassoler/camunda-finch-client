package com.finch.camunda.client.ws.xgracco;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finch.camunda.client.configurations.handler.exception.IntegrationException;
import com.finch.camunda.client.configurations.locale.MessageLocale;
import com.finch.camunda.client.domains.dtos.integracao.DecisorEntityDTO;
import com.finch.camunda.client.domains.dtos.integracao.LoginDTO;
import com.finch.camunda.client.domains.enums.EnumStatusDecisor;
import com.finch.camunda.client.domains.filters.DecisorEntityFilter;
import com.finch.camunda.client.legacy.EncryptionUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class XgraccoWsService {

    @Value(value = "${jobs.sincronismo.xgracco-url}")
    private String xgraccoUrl;

    @Value(value = "${jobs.sincronismo.username}")
    private String username;

    @Value(value = "${jobs.sincronismo.password}")
    private String password;

    @Autowired
    private RetryTemplate retryTemplate;

    private final RestTemplate restTemplate;
    private final MessageLocale messageLocale;
    private final ObjectMapper objectMapper;

    public XgraccoWsService(RestTemplate restTemplate, MessageLocale messageLocale, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.messageLocale = messageLocale;
        this.objectMapper = objectMapper;
    }

    public Long countDecisorEntities() throws JsonProcessingException, URISyntaxException {
        String token = this.buscarTokenAutenticacao();
        if (Objects.isNull(token)) {
            throw new IntegrationException(this.messageLocale.validationMessageSource("ws.xgracco.return.get.token.failure"));
        }
        String urlApi = this.xgraccoUrl + "/api/decisors/entity/count";
        log.info("Integração X-Gracco: Contagem dos Decisors com status GERADO na URL " + urlApi + ".");
        Set<EnumStatusDecisor> status = new HashSet<>();
        status.add(EnumStatusDecisor.GERADO);
        DecisorEntityFilter filter = DecisorEntityFilter.builder().status(status).build();
        URI uri = new URI(urlApi);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<DecisorEntityFilter> request = new HttpEntity<>(filter, headers);
        ParameterizedTypeReference<Long> responseType = new ParameterizedTypeReference<>() {
        };
        return this.retryTemplate.execute(c -> {
                    ResponseEntity<Long> result = restTemplate.exchange(urlApi, HttpMethod.GET, request, responseType);
                    Long retorno = Long.valueOf(result.getBody());
                    return retorno;
                },
                context -> this.recoverCountDecisorEntities(this.messageLocale.validationMessageSource("ws.xgracco.return.count.decisors"), context)
        );
    }

    public Long recoverCountDecisorEntities(String message, RetryContext context) {
        log.error(message, context.getLastThrowable());
        return Long.valueOf(0);
    }

    public List<DecisorEntityDTO> getDecisorEntities() throws JsonProcessingException, URISyntaxException {
        String token = this.buscarTokenAutenticacao();
        if (Objects.isNull(token)) {
            throw new IntegrationException(this.messageLocale.validationMessageSource("ws.xgracco.return.get.token.failure"));
        }
        String urlApi = this.xgraccoUrl + "/api/decisors/entity/status/" + EnumStatusDecisor.GERADO;
        log.info("Integração X-Gracco: Busca dos Decisors com status GERADO na URL " + urlApi + ".");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ParameterizedTypeReference<List<DecisorEntityDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        return this.retryTemplate.execute(c -> {
                    ResponseEntity<List<DecisorEntityDTO>> result = restTemplate.exchange(urlApi, HttpMethod.GET, entity, responseType);
                    List<DecisorEntityDTO> retorno = result.getBody();
                    return retorno;
                },
                context -> this.recoverGetDecisorEntities(this.messageLocale.validationMessageSource("ws.xgracco.return.get.decisors"), context)
        );
    }

    public List<DecisorEntityDTO> recoverGetDecisorEntities(String message, RetryContext context) {
        log.error(message, context.getLastThrowable());
        return new ArrayList<>();
    }

    public String buscarTokenAutenticacao() throws URISyntaxException, JsonProcessingException {
        final String urlApi = this.xgraccoUrl + "/api/authentication";
        log.info("Integração X-Gracco: Buscando o token na URL " + urlApi + ".");
        EncryptionUtil.setKey();
        LoginDTO login = LoginDTO.builder().username(this.username).password(EncryptionUtil.decrypt(this.password)).build();
        URI uri = new URI(urlApi);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginDTO> request = new HttpEntity<>(login, headers);
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<>() {
        };

        return this.retryTemplate.execute(c -> {
                    ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
                    String contentAsString = result.getBody().toString();
                    JsonNode jsonNode = objectMapper.readTree(contentAsString);
                    String token = jsonNode.get("token").get("access_token").asText();
                    return token;
                },
                context -> this.recoverPAutenticacao(this.messageLocale.validationMessageSource("ws.xgracco.return.get.token.failure"), context)
        );
    }

    public String recoverPAutenticacao(String mensagem, RetryContext context) {
        log.error(mensagem, context.getLastThrowable());
        return null;
    }

}
