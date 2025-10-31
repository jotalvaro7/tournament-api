package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTeamGoalsException extends TeamDomainException{
    public InvalidTeamGoalsException() {
        super("Invalid number of goals. Goals must be a non-negative number.", DomainErrorType.VALIDATION_ERROR);
    }
}
