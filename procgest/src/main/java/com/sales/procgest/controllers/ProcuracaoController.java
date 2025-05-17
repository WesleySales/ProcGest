package com.sales.procgest.controllers;

import com.sales.procgest.DTO.EstatisticasDTO;
import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.DTO.RelatorioDTO;
import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.entities.StatusProcuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import com.sales.procgest.services.EmailService;
import com.sales.procgest.services.ProcuracaoService;
import com.sales.procgest.services.RelatorioService;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Arquivo não é um PDF válido.");
        }

        File tempFile = File.createTempFile("procuracao", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }
        System.out.println("exibindo arquivo temporario"+tempFile);

        Procuracao data = procuracaoService.extrairDadosPdf(tempFile);
        System.out.println("\n"+data);
        if (data != null) {
            procuracaoRepository.save(data);
            emailService.gerarEmailCadastroProcuracao(data);
            tempFile.delete();
        }

        if (data == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Não foi possível extrair os dados do PDF.");
        }

        return ResponseEntity.ok().body(data);
    }

//    @GetMapping
//    public List<Procuracao> listarProcuracoes() {
//        return procuracaoService.listarProcuracoesPendentes() ;
//    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasDTO> exibirEstatisticas(){
        var estatisticas = procuracaoService.buscarEstatisticas();
        return ResponseEntity.ok().body(estatisticas);
    }

    @GetMapping
    public ResponseEntity<?> exibirProcuracoes(){
        return ResponseEntity.ok().body(relatorioService.gerarRelatorioGeral());
    }

    @PostMapping("/upload-multiplos")
    public ResponseEntity<?> uploadMultiplosPdfs(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<Procuracao> procuracoes = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().endsWith(".pdf")) {
                continue; // pula arquivos não-PDF
            }

            File tempFile = File.createTempFile("procuracao", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }

            Procuracao procuracao = procuracaoService.extrairDadosPdf(tempFile);
            tempFile.delete();

            if (procuracao != null) {
                procuracoes.add(procuracao);
                procuracaoRepository.save(procuracao);
            }
        }

        if (procuracoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Nenhum dado válido foi extraído.");
        }

        return ResponseEntity.ok(procuracoes);
    }

    @PostMapping("/testePDF")
    public ResponseEntity<?> testePdfEmail() {
        List<ProcuracaoDTO> lista = procuracaoService.gerarRelatorioVencimento(30l);
        String destinatario = "w.sales@ba.estudante.senai.br";

        emailService.enviarRelatorioVencimentos30DPdf(lista,destinatario);
        return ResponseEntity.ok().body("Email enviado com sucesso");
    }

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