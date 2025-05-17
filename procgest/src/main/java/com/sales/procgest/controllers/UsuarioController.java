package com.sales.procgest.controllers;

import com.sales.procgest.DTO.CadastroDTO;
import com.sales.procgest.DTO.LoginResponseDTO;
import com.sales.procgest.DTO.UsuarioDTO;
import com.sales.procgest.entities.Usuario;
import com.sales.procgest.infra.services.TokenService;
import com.sales.procgest.repositories.UsuarioRepository;
import com.sales.procgest.services.UsuarioService;
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
    private UsuarioService usuarioService ;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody @Valid CadastroDTO data){
        if(usuarioService.buscarPorLogin(data.login())) return ResponseEntity.badRequest()
                .body("Já existe um usuario com este login. Altere e tente novamente");

        try {
            usuarioService.cadastrarUsuario(data);
            return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao cadastrar o usuário. Tente novamente.");
        }
    }

}

