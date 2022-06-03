package com.finch.camunda.client.domains.dtos.integracao;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetornoIntegracaoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3913257642965133854L;

    private String retorno;


}
