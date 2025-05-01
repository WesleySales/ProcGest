package com.sales.procgest.entities;

public enum StatusProcuracao {
    PENDENTE("Pendente"),
    CONCLUIDO("Concluido"),
    EXPIRADO("Expirado");

    private final String descricao;

    StatusProcuracao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}