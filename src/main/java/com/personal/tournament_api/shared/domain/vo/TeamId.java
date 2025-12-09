package com.personal.tournament_api.shared.domain.vo;

public record TeamId(Long value) {

    public TeamId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Team ID must be a positive number");
        }
    }
}
