package com.finch.camunda.client.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finch.camunda.client.domains.dtos.integracao.IntegretionDecisorDTO;
import com.finch.camunda.client.legacy.EncryptionUtil;
import com.finch.camunda.client.services.IntegracaoXgraccoService;
import java.util.ArrayList;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("getDecisorEntity")
@Log4j2
public class GetDecisorEntity  implements ExternalTaskHandler {

    private final IntegracaoXgraccoService service;
    private final ObjectMapper objectMapper;

    public GetDecisorEntity(IntegracaoXgraccoService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Long contador = (Long) externalTask.getVariable("contador");
        String keyProcess = externalTask.getVariable("keyProcess");
        log.info("Executando a busca dos Decisors aguardando processamento. KeyProcess do fluxo: " + keyProcess + ".");
        IntegretionDecisorDTO integretionDecisorDTO = IntegretionDecisorDTO.builder().decisorEntities(new ArrayList<>()).build();
        try {
            integretionDecisorDTO = this.service.getDecisorEntitiesStatusGerado();
        } catch (Exception e){
            log.error("Falha na comunicação com o X-Gracco. Mensagem: " + e.getMessage(), e);
        }
        String retorno = null;
        try {
            retorno = this.objectMapper.writeValueAsString(integretionDecisorDTO);
        } catch (Exception e){
            log.error("Falha na conversão do Json em String. Processo GetDecisorEntity. Mensagem: " + e.getMessage(), e);
        }
        EncryptionUtil.setKey();
        String encrypRetorno = EncryptionUtil.encrypt(retorno);
        VariableMap variaveis = Variables.createVariables();
        variaveis.put("jsonDecisors", encrypRetorno);
        variaveis.put("keyProcess", keyProcess);
        externalTaskService.complete(externalTask, variaveis);
    }
}
