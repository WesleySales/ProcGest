package com.sales.procgest.DTO;

import com.sales.procgest.entities.StatusProcuracao;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record ProcuracaoDTO (
        Long id,
        String nomeProcurador,
        LocalDate dataInicio,
        LocalDate dataVencimento,
        String status
){
}
