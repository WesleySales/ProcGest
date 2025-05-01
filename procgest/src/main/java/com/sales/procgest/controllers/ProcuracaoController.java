package com.sales.procgest.controllers;

import com.sales.procgest.DTO.EmailDTO;
import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.entities.StatusProcuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import com.sales.procgest.services.EmailService;
import com.sales.procgest.services.ProcuracaoService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/procuracoes")
public class ProcuracaoController {

    @Autowired
    ProcuracaoService procuracaoService;

    @Autowired
    private ProcuracaoRepository procuracaoRepository;

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

    @GetMapping
    public List<Procuracao> listarProcuracoes() {
        return procuracaoRepository.findAll();
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<Procuracao> buscarProcuracaoPorId(@PathVariable Long id) {
        var procuracao = procuracaoRepository.findById(id).get();
        return ResponseEntity.ok().body(procuracao);
    }

    @PostMapping(value = "/{id}/attStatus")
    public ResponseEntity<Procuracao> atualizarStatus(@PathVariable Long id, @RequestBody ProcuracaoDTO data){
        var procuracao = procuracaoRepository.findById(id).get();
        String status = data.status().toUpperCase();
        procuracao.setStatus(StatusProcuracao.valueOf(status));
        procuracaoRepository.save(procuracao);
//        procuracaoService.atualizarStatusProcuracao(procuracao,status);
        return ResponseEntity.ok().body(procuracao);
    }

}