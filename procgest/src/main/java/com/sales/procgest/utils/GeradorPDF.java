package com.sales.procgest.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.entities.Procuracao;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class GeradorPDF {

    public byte[] gerarRelatorioProcuracoes(List<ProcuracaoDTO> lista) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("Procuracoes que vencerão nos próximos 30 dias"));
        document.add(new Paragraph(" "));
        document.add(
                new Paragraph("documento atualizado "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        );
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4); // 4 colunas

        Stream.of( "Procurador","Dias Para Vencer", "Início", "Vencimento")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(header));
                    cell.setBackgroundColor(Color.LIGHT_GRAY);
                    table.addCell(cell);
                });

        for (ProcuracaoDTO p : lista) {
            table.addCell(p.nomeProcurador());
            table.addCell(String.valueOf(p.diasParaVencer()));
            table.addCell(p.dataInicio());
            table.addCell(p.dataVencimento());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }


}
