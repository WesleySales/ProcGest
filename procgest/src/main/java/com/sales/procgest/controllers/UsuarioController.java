package com.sales.procgest.controllers;

import com.sales.procgest.DTO.CadastroDTO;
import com.sales.procgest.DTO.LoginResponseDTO;
import com.sales.procgest.DTO.UsuarioDTO;
import com.sales.procgest.entities.Usuario;
import com.sales.procgest.infra.services.TokenService;
import com.sales.procgest.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository ;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody @Valid CadastroDTO data){
        if(this.usuarioRepository.findByLogin(data.login())!=null) return ResponseEntity.badRequest()
                .body("Esse login não está disponível, tente novamente.");

        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());
        var user = new Usuario(data.login(), senhaCriptografada, data.role());

        try {
            usuarioRepository.save(user);
            return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao criar procuração: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("Erro ao cadastrar o usuário. Tente novamente.");
        }

    }

}

