package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TeamNotFoundException extends TeamDomainException{
    public TeamNotFoundException(Long teamId) {
        super("Team with id " + teamId + " not found", DomainErrorType.NOT_FOUND);
    }
}
