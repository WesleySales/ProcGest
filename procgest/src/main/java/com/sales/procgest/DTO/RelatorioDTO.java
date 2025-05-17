package com.sales.procgest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sales.procgest.entities.Procuracao;

import java.util.List;

public record RelatorioDTO(
        String titulo,
        String descricao,

        @JsonProperty(value = "Estatisticas")
        EstatisticasDTO estatisticasDTO,

        @JsonProperty("Lista de Procurações")
        List<ProcuracaoDTO> listaDeProcuracoes
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
    public EstatisticasDTO estatisticasDTO() {
        return estatisticasDTO;
    }

    @Override
    public List<ProcuracaoDTO> listaDeProcuracoes() {
        return listaDeProcuracoes;
    }
}
