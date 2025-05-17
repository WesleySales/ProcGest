package com.sales.procgest.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //Habilita a configuração do WebSecurity para ser configurada nessa classe
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable()) //desabilita configuração padrão
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/procuracoes/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/procuracoes/upload-multiplos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/procuracoes/{id}/attStatus").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/procuracoes/estatisticas").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/procuracoes/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET,"/procuracoes").authenticated()

                        .requestMatchers("procuracao/form").authenticated()

                        .requestMatchers("/usuarios/*").permitAll()
                        .requestMatchers("/auth/*").permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
