package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchTeamsException extends MatchDomainException {
    public InvalidMatchTeamsException() {
        super("The teams are invalid. Both team IDs must be positive and different from each other", DomainErrorType.VALIDATION_ERROR);
    }
}
