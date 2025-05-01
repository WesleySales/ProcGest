package com.sales.procgest.services;

import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.entities.StatusProcuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProcuracaoService {

    @Autowired
    private ProcuracaoRepository procuracaoRepository;

    public Procuracao extrairDadosPdf(File file) {
        try(PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setParagraphStart(" ");
            stripper.setLineSeparator(" ");
            String text = stripper.getText(document).replaceAll("\\s+", " ");

            // Extrair nome do procurador
            Pattern nomePattern = Pattern.compile("mandato,\\s*(.+?),\\s*doravante denominado\\s*PROCURADOR", Pattern.CASE_INSENSITIVE);
            Matcher nomeMatcher = nomePattern.matcher(text);

            // Extrair quantidade de meses
            Pattern mesesPattern = Pattern.compile("vigência pelo período atual até (\\d{1,2}) meses", Pattern.CASE_INSENSITIVE);
            Matcher mesesMatcher = mesesPattern.matcher(text);

            // Extrair data de início se houver
            Pattern inicioPattern = Pattern.compile("Data de In[ií]cio:\\s*(\\d{2}/\\d{2}/\\d{4})", Pattern.CASE_INSENSITIVE);
            Matcher inicioMatcher = inicioPattern.matcher(text);

            if (nomeMatcher.find() && mesesMatcher.find()) {
                String nome = nomeMatcher.group(1).trim();

                // Definir data de início
                LocalDate inicio;
                if (inicioMatcher.find()) {
                    inicio = LocalDate.parse(inicioMatcher.group(1), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } else {
                    inicio = LocalDate.now(); // padrão se não estiver no texto
                }

                int meses = Integer.parseInt(mesesMatcher.group(1));
                LocalDate vencimento = inicio.plusMonths(meses);

                return new  Procuracao(nome, inicio, vencimento);
            }

        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private Procuracao salvarProcuracao(Procuracao procuracao){
        return procuracaoRepository.save(procuracao);
    }

//    public void atualizarStatusProcuracao(Procuracao procuracao, String status){
//        procuracao.setStatus(StatusProcuracao.valueOf(status.toUpperCase()));
//    }


}
