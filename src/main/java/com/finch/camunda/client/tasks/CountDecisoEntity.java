package com.finch.camunda.client.tasks;

import com.finch.camunda.client.services.IntegracaoXgraccoService;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription("countDecisorEntity")
@Log4j2
public class CountDecisoEntity implements ExternalTaskHandler {

    private final IntegracaoXgraccoService service;

    public CountDecisoEntity(IntegracaoXgraccoService service) {
        this.service = service;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        String keyProcess = UUID.randomUUID().toString();
        log.info("Executando processo do fluxo do Decisor. KeyProcess do fluxo:: " + keyProcess + ".");
        log.info("Verificando registros do Decisor aguardando para processamento.");
        Long contador = Long.valueOf(0);
        try {
            contador = this.service.countDecisorEntitiesStatusGerado();
        } catch (Exception e){
            log.error("Falha na comunicação com o X-Gracco. Mensagem: " + e.getMessage(), e);
        }
        VariableMap variaveis = Variables.createVariables();
        String mensagem = "Teste de execução do camunda - Count().";
        variaveis.put("contador", contador);
        variaveis.put("keyProcess", keyProcess);
        externalTaskService.complete(externalTask, variaveis);
    }
}
