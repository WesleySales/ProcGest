package com.sales.procgest;

import com.sales.procgest.DTO.CadastroDTO;
import com.sales.procgest.DTO.ProcuracaoDTO;
import com.sales.procgest.DTO.UsuarioDTO;
import com.sales.procgest.entities.Usuario;
import com.sales.procgest.repositories.ProcuracaoRepository;
import com.sales.procgest.services.EmailService;
import com.sales.procgest.services.ProcuracaoService;
import com.sales.procgest.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ProcgestApplication implements CommandLineRunner {

	@Autowired
	private UsuarioService usuarioService;


	public static void main(String[] args) {
		SpringApplication.run(ProcgestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


//		CadastroDTO data = new CadastroDTO("Wesley","sales8080","ADMIN");
//
//		usuarioService.cadastrarUsuario(data);

	}
}
