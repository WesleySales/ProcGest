package com.sales.procgest.services;

import com.sales.procgest.DTO.EstatisticasDTO;
import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.DTO.RelatorioDTO;
import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.entities.StatusProcuracao;
import com.sales.procgest.repositories.ProcuracaoRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public List<ProcuracaoDTO> listarProcuracoesPendentes(){
        var lista = procuracaoRepository.findByStatus(StatusProcuracao.PENDENTE);
        List<ProcuracaoDTO> listaDTO = new ArrayList<>();
        for(Procuracao p: lista){
            var pDTO = getProcuracaoDTO(p);
            listaDTO.add(pDTO);
        }

        return listaDTO.stream()
                .sorted(Comparator.comparing(ProcuracaoDTO::diasParaVencer))
                .collect(Collectors.toList());
    }

    public List<ProcuracaoDTO> gerarRelatorioVencimento(Long dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);

        List<Procuracao> lista = procuracaoRepository.findByDataVencimentoBetween(hoje, limite);

        List<ProcuracaoDTO> listaDTO = new ArrayList<>();
        for(Procuracao p: lista){
            var pDTO = getProcuracaoDTO(p);
            listaDTO.add(pDTO);
        }
        // ordenar por vencimento mais próximo
        return listaDTO.stream()
                .sorted(Comparator.comparing(ProcuracaoDTO::diasParaVencer))
                .collect(Collectors.toList());
    }

    public EstatisticasDTO buscarEstatisticas(){

        LocalDate dataFim = LocalDate.now().plusDays(30);
        List<Procuracao> listaRecuperada = procuracaoRepository.findAll();

        int total = listaRecuperada.size();
        int pendentes = (int) listaRecuperada.stream()
                .filter(p -> p.getStatus() == StatusProcuracao.PENDENTE)
                .count();
        int expiradas = (int) listaRecuperada.stream()
                .filter(p -> p.getStatus() == StatusProcuracao.EXPIRADO)
                .count();
        int aVencer = (int) listaRecuperada.stream()
                .filter(p -> {
                    LocalDate vencimento = p.getDataVencimento();
                    return vencimento != null &&
                            !vencimento.isBefore(LocalDate.now()) &&
                            !vencimento.isAfter(dataFim);
                })
                .count();


//        int total = procuracaoRepository.findAll().size();
//        int pendentes = procuracaoRepository.findByStatus(StatusProcuracao.PENDENTE).size();
//        int expiradas = procuracaoRepository.findByStatus(StatusProcuracao.EXPIRADO).size();
//        int aVencer = procuracaoRepository.findByDataVencimentoBetween(LocalDate.now(),dataFim).size();

        EstatisticasDTO estatisticas = new EstatisticasDTO(total,pendentes,expiradas,aVencer);

        return estatisticas;
    }

    //Funcao para transformar procuracao em procuracaoDTO
    public ProcuracaoDTO getProcuracaoDTO(Procuracao procuracao){
        String nomeProcurador = procuracao.getNomeProcurador();
        String dataInicio = procuracao.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dataVencimento = procuracao.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), procuracao.getDataVencimento());
        String status = procuracao.getStatus().name();

        return new ProcuracaoDTO(nomeProcurador,dataInicio,dataVencimento,diasParaVencer,status);
    }



}
