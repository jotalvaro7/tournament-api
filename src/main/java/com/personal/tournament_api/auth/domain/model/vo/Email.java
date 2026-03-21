package com.personal.tournament_api.auth.domain.model.vo;

import com.personal.tournament_api.auth.domain.exceptions.InvalidEmailException;

public record Email(String value) {

    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidEmailException("Email cannot be blank");
        }
        if (!value.matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidEmailException("Invalid email format: " + value);
        }
    }
}