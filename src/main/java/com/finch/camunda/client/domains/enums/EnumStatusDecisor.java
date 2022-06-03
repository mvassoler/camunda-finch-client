package com.finch.camunda.client.domains.enums;

import java.util.stream.Stream;

public enum EnumStatusDecisor implements PersistentEnum {

    GERADO(1, "Gerado"),
    EXECUTADO(2, "Executado"),
    CANCELADO(3, "Cancelado");

    private final int id;
    private final String descricao;

    EnumStatusDecisor(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static EnumStatusDecisor getById(int id) {
        return Stream.of(EnumStatusDecisor.values())
                .filter(statusEvento -> statusEvento.getId() == id).findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(EnumStatusDecisor.class, String.valueOf(id)));
    }

    public static EnumStatusDecisor getByDescricao(final String descricao) {
        if (descricao == null) {
            return null;
        }
        return Stream.of(EnumStatusDecisor.values())
                .filter(statusEvento -> statusEvento.getDescricao().trim().toUpperCase().equals(descricao.trim().toUpperCase())).findAny()
                .orElseThrow(() -> new EnumConstantNotPresentException(EnumStatusDecisor.class, descricao));
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getDescricao() {
        return descricao;
    }
}
