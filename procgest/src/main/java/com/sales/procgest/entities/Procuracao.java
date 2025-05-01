package com.sales.procgest.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Procuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeProcurador;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusProcuracao status;

    public Procuracao(){

    }

    public Procuracao(String nomeProcurador, LocalDate dataInicio, LocalDate dataVencimento) {
        this.nomeProcurador = nomeProcurador;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.status = StatusProcuracao.PENDENTE;
    }

    public String getNomeProcurador() {
        return nomeProcurador;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setStatus(StatusProcuracao status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public StatusProcuracao getStatus() {
        return status;
    }
}
