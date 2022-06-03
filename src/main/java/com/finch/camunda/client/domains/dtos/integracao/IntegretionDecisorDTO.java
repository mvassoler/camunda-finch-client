package com.finch.camunda.client.domains.dtos.integracao;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntegretionDecisorDTO implements Serializable {

    private List<DecisorEntityDTO> decisorEntities;

}
