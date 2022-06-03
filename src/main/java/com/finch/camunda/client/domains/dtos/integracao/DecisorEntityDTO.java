package com.finch.camunda.client.domains.dtos.integracao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.finch.camunda.client.domains.enums.EnumStatusDecisor;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecisorEntityDTO implements Serializable {

    private static final long serialVersionUID = 8388708055296492685L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("tabela")
    private String tabela;

    @JsonProperty("evento")
    private String evento;

    @JsonProperty("id_registro")
    private Long idRegistro;

    @JsonProperty("status")
    private EnumStatusDecisor enumStatusDecisor;

}
