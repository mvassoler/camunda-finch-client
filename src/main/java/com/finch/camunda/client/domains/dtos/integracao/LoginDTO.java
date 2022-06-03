package com.finch.camunda.client.domains.dtos.integracao;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3438905978815474008L;

    private String username;
    private String password;

}