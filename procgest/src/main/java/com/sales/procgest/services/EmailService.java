package com.sales.procgest.services;

import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.entities.Procuracao;
import com.sales.procgest.utils.GeradorPDF;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.stringtemplate.v4.ST;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private GeradorPDF geradorPDF;

    @Autowired
    private ProcuracaoService procuracaoService;

    @Autowired
    private Environment env;

    private void enviarEmailSimples(String para, String assunto, String corpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject(assunto);
        message.setText(corpo);
        message.setFrom(env.getProperty("spring.mail.username")); // pega do application.properties

        mailSender.send(message);

        System.out.println("Email enviado com sucesso para "+ para);
    }

    public void gerarEmailCadastroProcuracao(Procuracao procuracao){
        String para = "w.sales@ba.estudante.senai.br";
        String assunto = "PROCURAÇÃO CADASTRADA COM SUCESSO";
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), procuracao.getDataVencimento());
        String corpo =
                "Prezado(a),\n\n" +
                        "Informamos que a procuração foi cadastrada com sucesso em nosso sistema. Seguem abaixo os detalhes:\n\n" +
                        "Nome do procurador:"+ procuracao.getNomeProcurador()+"\n" +
                        "Data de início da vigência: "+ procuracao.getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\n" +
                        "Data de vencimento: "+ procuracao.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+"\n" +
                        "Dias restantes até o vencimento: "+ diasRestantes+"\n\n" +
                        "Recomendamos atenção aos prazos para garantir que os atos praticados com base na presente procuração estejam dentro da validade estipulada.\n\n" +
                        "Em caso de dúvidas ou necessidade de atualização, estamos à disposição.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe Wesley Sales, A LENDA";
        enviarEmailSimples(para,assunto,corpo);
    }
//
//    public void gerarEmailVencimentos30D(Procuracao procuracao){
//        String para = "w.sales@ba.estudante.senai.br";
//        String assunto = "RELATORIO - VENCIMENTO NOS PROXIMOS 30 DIAS";
//        String corpo = "Segue a lista de procurações que vencem nos proximos 30 dias: ";
//        enviarEmailSimples(para,assunto,corpo);
//    }

    public void enviarRelatorioVencimentos30DPdf(List<ProcuracaoDTO> proximas, String destinatario) {

        try {
            byte[] pdf = geradorPDF.gerarRelatorioProcuracoes(proximas);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(destinatario);
            helper.setSubject("Relatório de Procurações Próximas do Vencimento");
            helper.setText(
                    "Prezado(a), segue em anexo o relatório de procurações que vencem em breve.\n\n" +
                    "att, Wesley Sales"
            );

            helper.addAttachment("relatorio.pdf", new ByteArrayResource(pdf));

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
