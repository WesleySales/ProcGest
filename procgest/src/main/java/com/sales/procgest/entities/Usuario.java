package com.sales.procgest.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario role;


    public Usuario() {
    }

    public Usuario(Long id, String login, String senha, TipoUsuario role) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.role = role;
    }

    public Usuario(String login, String senha, String role) {
        this.login = login;
        this.senha = senha;
        this.role = TipoUsuario.valueOf(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role==TipoUsuario.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

