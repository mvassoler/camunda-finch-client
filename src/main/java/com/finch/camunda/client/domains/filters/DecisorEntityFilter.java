package com.finch.camunda.client.domains.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finch.camunda.client.domains.entities.DecisorEntity;
import com.finch.camunda.client.domains.enums.EnumStatusDecisor;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecisorEntityFilter implements Filter<DecisorEntity> {

    @JsonProperty("tabelas")
    private Set<String> tabelas;

    @JsonProperty("status")
    private Set<EnumStatusDecisor> status;
}
