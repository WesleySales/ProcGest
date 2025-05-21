package com.sales.procgest.DTO;

import com.sales.procgest.entities.StatusProcuracao;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ProcuracaoDTO (
//        Long id,
        @NotBlank(message = "O nome do procurador não pode estar vazio")
        String nomeProcurador,

        @NotBlank(message = "A data de início não pode estar vazia")
        String dataInicio,

        @NotBlank(message = "A data de vencimento não pode estar vazia")
        String dataVencimento,

        Long diasParaVencer,
        String status
){
    @Override
    public Long diasParaVencer() {
        return diasParaVencer;
    }
}
