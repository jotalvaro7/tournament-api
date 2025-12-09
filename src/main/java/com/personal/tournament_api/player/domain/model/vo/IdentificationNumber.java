package com.personal.tournament_api.player.domain.model.vo;

import com.personal.tournament_api.player.domain.exceptions.InvalidPlayerIdentificationNumberException;

public record IdentificationNumber(String value) {

    public IdentificationNumber {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidPlayerIdentificationNumberException();
        }
    }
}
