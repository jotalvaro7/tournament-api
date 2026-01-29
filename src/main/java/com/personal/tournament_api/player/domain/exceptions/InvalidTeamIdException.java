package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.team.domain.exceptions.TeamDomainException;

public class InvalidTeamIdException extends TeamDomainException {

    public InvalidTeamIdException() {
        super("The team ID is invalid.", DomainErrorType.VALIDATION_ERROR);
    }
}
