package com.sales.procgest.DTO;

public record LoginResponseDTO(
        String token
) {
    @Override
    public String token() {
        return token;
    }
}
