package com.personal.tournament_api.player.domain.model.vo;

import com.personal.tournament_api.player.domain.exceptions.InvalidPlayerLastNameException;

public record PlayerLastName(String value) {

    public PlayerLastName {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidPlayerLastNameException();
        }
    }
}
