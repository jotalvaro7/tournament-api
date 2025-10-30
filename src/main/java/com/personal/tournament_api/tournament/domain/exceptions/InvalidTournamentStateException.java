package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTournamentStateException extends TournamentDomainException {
    public InvalidTournamentStateException(String message){
        super(message, DomainErrorType.VALIDATION_ERROR);
    }
}
