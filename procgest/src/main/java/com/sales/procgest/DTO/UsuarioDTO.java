package com.sales.procgest.DTO;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        @NotBlank(message = "O login deve ser preenchido")
        String login,

        @NotBlank(message = "A senha deve ser preenchida")
        String senha
) {
    public UsuarioDTO {
    }

    @Override
    public String login() {
        return login;
    }

    @Override
    public String senha() {
        return senha;
    }
}
