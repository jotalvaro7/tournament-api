package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchTournamentIdException extends MatchDomainException {
    public InvalidMatchTournamentIdException() {
        super("The tournament ID is invalid. It must be a positive number", DomainErrorType.VALIDATION_ERROR);
    }
}
