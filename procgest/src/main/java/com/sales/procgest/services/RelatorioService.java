package com.sales.procgest.services;

import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.DTO.RelatorioDTO;
import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.entities.StatusProcuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ProcuracaoRepository procuracaoRepository;

    @Autowired
    private ProcuracaoService procuracaoService;

    public RelatorioDTO gerarRelatorioGeral(){

        var estatisticas = procuracaoService.buscarEstatisticas();

        List<ProcuracaoDTO> lista = procuracaoService.listarProcuracoes();
        String titulo = "RELATÓRIO GERAL DE PROCURAÇÕES";
        String desc = "Exibindo relatório geral de procurações da mais proxima do vencimento para menos proxima";

        RelatorioDTO relatorioDTO = new RelatorioDTO(titulo,desc,estatisticas,lista);
        return relatorioDTO;
    }

//    public RelatorioDTO gerarRelatorioPorStatus(String status){
//        List<Procuracao> lista = procuracaoRepository.findByStatus(StatusProcuracao.valueOf(status.toUpperCase()));
//        for(Procuracao p:lista){
//            p.setDiasParaVencer(ChronoUnit.DAYS.between(LocalDate.now(), p.getDataVencimento()));
//        }
//        String titulo = "Relatorio - PROCURACOES POR STATUS: "+status.toUpperCase();
//        String desc = "Exibindo relatorio de procurações por status";
//        int numeroProcuracoes = lista.size();
//
//        RelatorioDTO relatorioDTO = new RelatorioDTO(titulo,desc,numeroProcuracoes,lista);
//        return relatorioDTO;
//    }
}
