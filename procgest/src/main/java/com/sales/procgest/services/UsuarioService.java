package com.sales.procgest.services;

import com.sales.procgest.DTO.CadastroDTO;
import com.sales.procgest.entities.Usuario;
import com.sales.procgest.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository ;

    public Usuario cadastrarUsuario(CadastroDTO data){
        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());
        var usuario = new Usuario(data.login(), senhaCriptografada, data.role());
        usuarioRepository.save(usuario);
        return usuario;
    }

    //verifica se o nome de login esta disponivel. true = disponivel;
    public boolean buscarPorLogin(String login){
        return usuarioRepository.findByLogin(login)!=null? true : false;
    }

}

