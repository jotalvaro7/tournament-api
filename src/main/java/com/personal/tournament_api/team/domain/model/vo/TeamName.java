package com.personal.tournament_api.team.domain.model.vo;

import com.personal.tournament_api.team.domain.exceptions.InvalidTeamNameException;

public record TeamName(String value) {

    public TeamName {
        if (value == null || value.trim().isEmpty() || value.length() < 3 || value.length() > 100) {
            throw new InvalidTeamNameException();
        }
    }
}