package com.sales.procgest.controllers;

import com.sales.procgest.DTO.CadastroDTO;
import com.sales.procgest.DTO.LoginResponseDTO;
import com.sales.procgest.DTO.UsuarioDTO;
import com.sales.procgest.DTO.UsuarioResponseDTO;
import com.sales.procgest.entities.Usuario;
import com.sales.procgest.infra.services.TokenService;
import com.sales.procgest.repositories.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
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

    @GetMapping("/carregarDadosUsuario")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioFromToken(@RequestHeader("Authorization") String authHeader) {
        // Extrai o token do header (remove "Bearer ")
        String token = authHeader.substring(7);
        System.out.println("Token Recebido: "+token);

        String login = tokenService.validarToken(token);
        if (login.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = usuarioRepository.findByLogin(login);

        UsuarioResponseDTO user = new UsuarioResponseDTO(userDetails.getUsername());

        return ResponseEntity.ok(user);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = extractToken(request);
//        if (token != null) {
//            tokenBlacklist.add(token); // Adiciona à blacklist
//        }
        return ResponseEntity.ok().build();
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer "
        }
        return null;
    }

}

