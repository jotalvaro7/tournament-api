package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTournamentDescriptionException extends TournamentDomainException {
    public InvalidTournamentDescriptionException() {
        super("Invalid tournament description. The description must be between 10 and 200 characters long.", DomainErrorType.VALIDATION_ERROR);
    }
}
