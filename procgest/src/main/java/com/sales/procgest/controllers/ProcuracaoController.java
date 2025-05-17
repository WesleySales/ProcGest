package com.sales.procgest.controllers;

import com.sales.procgest.DTO.EstatisticasDTO;
import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import com.sales.procgest.services.EmailService;
import com.sales.procgest.services.ProcuracaoService;
import com.sales.procgest.services.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/procuracoes")
public class ProcuracaoController {

    @Autowired
    ProcuracaoService procuracaoService;

    @Autowired
    private ProcuracaoRepository procuracaoRepository;

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMultiplosPdfs(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<Procuracao> procuracoes = procuracaoService.processarVariosPdfs(files);

        if (procuracoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Nenhum dado válido foi extraído.");
        }

        return ResponseEntity.status(201).body("Procurações cadastradas com sucesso!");
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasDTO> exibirEstatisticas(){
        var estatisticas = procuracaoService.buscarEstatisticas();
        return ResponseEntity.ok().body(estatisticas);
    }

    @GetMapping
    public ResponseEntity<?> exibirProcuracoes(){
        return ResponseEntity.ok().body(relatorioService.gerarRelatorioGeral());
    }

//    @PostMapping("/testePDF")
//    public ResponseEntity<?> testePdfEmail() {
//        List<ProcuracaoDTO> lista = procuracaoService.gerarRelatorioVencimento(30l);
//        String destinatario = "w.sales@ba.estudante.senai.br";
//
//        emailService.enviarRelatorioVencimentos30DPdf(lista,destinatario);
//        return ResponseEntity.ok().body("Email enviado com sucesso");
//    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> buscarProcuracaoPorId(@PathVariable Long id) {
        try{
            var proc = procuracaoService.buscarPorId(id);
            if(proc == null) return ResponseEntity.status(404).body("Procuração não encontrada, confira o ID passado!");
            return ResponseEntity.ok().body(proc);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }

//    @GetMapping
//    public ResponseEntity<?> buscarPorNomeProcurador(@RequestParam String nome){
//        List<?> procuracao = procuracaoRepository.findByNomeProcurador(nome);
//
//        return procuracao.isEmpty()?
//                ResponseEntity.badRequest().body("Nenhuma procuração em nome de "+nome):
//                ResponseEntity.ok(procuracao);
//    }

    @PostMapping(value = "/{id}/attStatus")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody ProcuracaoDTO data){
        try{
            var proc = procuracaoService.buscarPorId(id);
            if(proc == null) return ResponseEntity.status(404).body("Procuração não encontrada, confira o ID passado!");
            procuracaoService.atualizarStatusProcuracao(proc, data.status());
            return ResponseEntity.status(200).body("O status da procuração foi atualizado com sucesso!");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Error: "+e.getMessage());
        }
    }

    //Esse endpoint espera um filtro em dias direto na URL, assim puxa a lista de procurações a vencer neste prazo
    @GetMapping("/relatorio/vencimento/{dias}")
    public ResponseEntity<?> listarProcuracoesVencendo(@PathVariable Long dias) {
        List<ProcuracaoDTO> lista = procuracaoService.gerarRelatorioVencimento(dias);
        return ResponseEntity.ok().body(lista);
    }

}