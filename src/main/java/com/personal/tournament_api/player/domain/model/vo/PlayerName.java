package com.personal.tournament_api.player.domain.model.vo;

import com.personal.tournament_api.player.domain.exceptions.InvalidPlayerNameException;

public record PlayerName(String value) {

    public PlayerName {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidPlayerNameException();
        }
    }
}
