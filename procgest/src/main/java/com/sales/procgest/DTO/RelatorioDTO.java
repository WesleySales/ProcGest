package com.sales.procgest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sales.procgest.entities.Procuracao;

import java.util.List;

public record RelatorioDTO(
        String titulo,
        String descricao,

        @JsonProperty(value = "Número Total de Procurações")
        int totalProcuracoes,

        @JsonProperty("Lista de Procurações")
        List<Procuracao> listaDeProcuracoes
) {
    public RelatorioDTO {
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public String descricao() {
        return descricao;
    }

    @Override
    public int totalProcuracoes() {
        return totalProcuracoes;
    }

    @Override
    public List<Procuracao> listaDeProcuracoes() {
        return listaDeProcuracoes;
    }
}
