package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.shared.domain.exceptions.DomainException;

public class TournamentDomainException extends DomainException {
    public TournamentDomainException(String message, DomainErrorType errorType) {
        super(message, errorType);
    }
}
