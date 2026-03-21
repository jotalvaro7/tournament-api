package com.personal.tournament_api.tournament.domain.model.vo;

import com.personal.tournament_api.tournament.domain.exceptions.InvalidTournamentNameException;

public record TournamentName(String value) {

    public TournamentName {
        if (value == null || value.trim().isEmpty() || value.length() > 100) {
            throw new InvalidTournamentNameException();
        }
    }
}