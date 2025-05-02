package com.sales.procgest.controllers;

import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import com.sales.procgest.services.ProcuracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping(value = "procuracao")
public class ProcuracaoWebController {

    @Autowired
    private ProcuracaoService procuracaoService;

    @Autowired
    private ProcuracaoRepository procuracaoRepository;

    @GetMapping("/procuracao/form")
    public String mostrarFormulario(Model model) {
        model.addAttribute("procuracao", new Procuracao());
        return "cadastro";
    }

    @PostMapping("/procuracao/form")
    public String cadastrarProcuracao(@ModelAttribute Procuracao procuracao, Model model) {
        procuracaoRepository.save(procuracao);
        model.addAttribute("mensagem", "Procuração cadastrada com sucesso!");
        return "cadastro";
    }

    @PostMapping("procuracao/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("files") List<MultipartFile> files) throws IOException {
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
//                emailService.gerarEmailCadastroProcuracao(procuracao);
            }
        }

        if (procuracoes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Nenhum dado válido foi extraído.");
        }

        return ResponseEntity.ok(procuracoes);
    }
}

