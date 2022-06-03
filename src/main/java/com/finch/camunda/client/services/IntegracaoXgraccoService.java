package com.finch.camunda.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finch.camunda.client.domains.dtos.integracao.DecisorEntityDTO;
import com.finch.camunda.client.domains.dtos.integracao.IntegretionDecisorDTO;
import com.finch.camunda.client.ws.xgracco.XgraccoWsService;
import java.net.URISyntaxException;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class IntegracaoXgraccoService {

    private final XgraccoWsService service;

    public IntegracaoXgraccoService(XgraccoWsService service) {
        this.service = service;
    }

    public Long countDecisorEntitiesStatusGerado() throws URISyntaxException, JsonProcessingException {
        return this.service.countDecisorEntities();
    }

    public IntegretionDecisorDTO getDecisorEntitiesStatusGerado() throws URISyntaxException, JsonProcessingException {
        List<DecisorEntityDTO> decisors = this.service.getDecisorEntities();
        IntegretionDecisorDTO integration = IntegretionDecisorDTO.builder()
                .decisorEntities(decisors)
                .build();
        return integration;
    }
}
