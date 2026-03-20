package com.personal.tournament_api.match.domain.model.vo;

import com.personal.tournament_api.match.domain.exceptions.InvalidMatchFieldException;

public record MatchField(String value) {

    public MatchField {
        if (value == null || value.trim().isEmpty() || value.length() > 100) {
            throw new InvalidMatchFieldException();
        }
    }
}