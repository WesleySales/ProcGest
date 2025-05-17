package com.sales.procgest.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Procuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String nomeProcurador;
    private LocalDate dataInicio;
    private LocalDate dataVencimento;

    @Transient
    private Long diasParaVencer;

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

    public Long getDiasParaVencer() {
        return diasParaVencer;
    }

    public void setDiasParaVencer(Long diasParaVencer) {
        this.diasParaVencer = diasParaVencer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeProcurador(String nomeProcurador) {
        this.nomeProcurador = nomeProcurador;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


}
