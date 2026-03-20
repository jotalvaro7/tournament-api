package com.personal.tournament_api.tournament.domain.model.vo;

import com.personal.tournament_api.tournament.domain.exceptions.InvalidTournamentDescriptionException;

public record TournamentDescription(String value) {

    public TournamentDescription {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidTournamentDescriptionException();
        }
    }
}