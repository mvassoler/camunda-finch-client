package com.finch.camunda.client.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finch.camunda.client.domains.dtos.integracao.IntegretionDecisorDTO;
import com.finch.camunda.client.legacy.EncryptionUtil;
import com.finch.camunda.client.services.IntegracaoXgraccoService;
import java.util.ArrayList;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("processDecisorEntity")
@Log4j2
public class ProcessDecisorEntity implements ExternalTaskHandler {

    private final IntegracaoXgraccoService service;
    private final ObjectMapper objectMapper;

    public ProcessDecisorEntity(IntegracaoXgraccoService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String jsonDecisors = externalTask.getVariable("jsonDecisors");
        String keyProcess = externalTask.getVariable("keyProcess");
        log.info("Executando o processamento dos Decisors aguardando processamento. KeyProcess do fluxo: " + keyProcess + ".");
        IntegretionDecisorDTO integracao = IntegretionDecisorDTO.builder().decisorEntities(new ArrayList<>()).build();
        EncryptionUtil.setKey();
        String descryptRetorno = EncryptionUtil.decrypt(jsonDecisors);
        try {
            integracao =  objectMapper.readValue(descryptRetorno, IntegretionDecisorDTO.class);
        } catch (Exception e){
            log.error("Falha na conversão da String em Json. Processo ProcessDecisorEntity. Mensagem: " + e.getMessage(), e);
        }
        if(Objects.nonNull(integracao)){
            if(Objects.nonNull(integracao.getDecisorEntities()) && CollectionUtils.isNotEmpty(integracao.getDecisorEntities())){
                //TODO - IMPLEMENTAR O PROCESSAMENTO
            }
        }
        VariableMap variaveis = Variables.createVariables();
        variaveis.put("keyProcess", keyProcess);
        externalTaskService.complete(externalTask, variaveis);
    }
}
