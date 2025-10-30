package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTournamentNameException extends TournamentDomainException {
    public InvalidTournamentNameException() {
        super("Invalid tournament name. The name must be between 3 and 50 characters long.", DomainErrorType.VALIDATION_ERROR);
    }
}
