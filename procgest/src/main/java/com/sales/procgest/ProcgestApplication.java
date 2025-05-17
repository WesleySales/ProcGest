package com.sales.procgest;

import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.repositories.ProcuracaoRepository;
import com.sales.procgest.services.EmailService;
import com.sales.procgest.services.ProcuracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProcgestApplication implements CommandLineRunner {

	@Autowired
	private EmailService emailService;

	@Autowired
	private ProcuracaoService procuracaoService;

	@Autowired
	private ProcuracaoRepository procuracaoRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProcgestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//
//		List<?> procuracao = procuracaoRepository.findByNomeProcurador("Daniel Souza");
//		System.out.println("As procurações de Daniel Souza: \n"+procuracao);
	}

}
