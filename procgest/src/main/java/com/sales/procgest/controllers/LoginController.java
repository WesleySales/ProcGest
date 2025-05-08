package com.sales.procgest.controllers;

import com.sales.procgest.DTO.CadastroDTO;
import com.sales.procgest.DTO.LoginResponseDTO;
import com.sales.procgest.DTO.UsuarioDTO;
import com.sales.procgest.entities.Usuario;
import com.sales.procgest.infra.services.TokenService;
import com.sales.procgest.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository ;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO data){
        var usernamePassowrd = new UsernamePasswordAuthenticationToken(data.login(),data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassowrd);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody CadastroDTO data){
        if(this.usuarioRepository.findByLogin(data.login())!=null) return ResponseEntity.badRequest().build();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());
        var user = new Usuario(data.login(), senhaCriptografada, data.role());
        usuarioRepository.save(user);
        return ResponseEntity.ok().build();
    }

}

