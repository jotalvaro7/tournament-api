package com.personal.tournament_api.team.domain.model.vo;

import com.personal.tournament_api.team.domain.exceptions.InvalidTeamCoachException;

public record CoachName(String value) {

    public CoachName {
        if (value == null || value.trim().isEmpty() || value.length() < 3 || value.length() > 100) {
            throw new InvalidTeamCoachException();
        }
    }
}