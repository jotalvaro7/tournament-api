package com.personal.tournament_api.auth.infrastructure.adapters.web.dto;

import com.personal.tournament_api.auth.domain.enums.UserRole;

public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String username,
        UserRole role
) {
    public static AuthResponse of(String token, String email, String username, UserRole role) {
        return new AuthResponse(token, "Bearer", email, username, role);
    }
}