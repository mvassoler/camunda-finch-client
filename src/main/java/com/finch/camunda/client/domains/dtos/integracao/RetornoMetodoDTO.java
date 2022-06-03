package com.finch.camunda.client.domains.dtos.integracao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetornoMetodoDTO implements Serializable {

    private boolean sucesso;
    private String mensagem;
    private String pagina;
    private String classe;
    private Long idGerado;
    private String valor;
    private String exception;
    private Object objeto;
    private List<Object> objetos;
    private Map<String, Object> chaveValor;

}
