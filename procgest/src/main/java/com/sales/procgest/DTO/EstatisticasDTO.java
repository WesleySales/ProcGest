package com.sales.procgest.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EstatisticasDTO(

        @JsonProperty(value = "Total de Procurações")
        int total,

        @JsonProperty(value = "Procurações Pendentes")
        int pendentes,

        @JsonProperty(value = "Procurações Expiradas")
        int expiradas,

        @JsonProperty(value = "Procurações a Vencer nos proximos 30 dias")
        int aVencer
) {
    public EstatisticasDTO {
    }
}
