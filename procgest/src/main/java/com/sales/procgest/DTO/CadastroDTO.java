package com.sales.procgest.DTO;

import jakarta.validation.constraints.NotBlank;

public record CadastroDTO (

        @NotBlank(message = "O login deve ser preenchido!")
        String login,

        @NotBlank(message = "A senha deve ser preenchida")
        String senha,

        @NotBlank(message = "A role do usuário deve ser preenchida")
        String role
) {
}
