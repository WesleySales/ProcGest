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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository ;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UsuarioDTO data){
        try {
            var usernamePassowrd = new UsernamePasswordAuthenticationToken(data.login(),data.senha());
            var auth = this.authenticationManager.authenticate(usernamePassowrd);
            var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body("Usuário ou senha incorretos");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro inesperado ao tentar logar: " + e.getMessage());
        }

    }

}

