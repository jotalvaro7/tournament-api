package com.personal.tournament_api.auth.domain.model.vo;

public record HashedPassword(String value) {

    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Hashed password cannot be blank");
        }
    }
}