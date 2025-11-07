package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchDateException extends MatchDomainException {
    public InvalidMatchDateException() {
        super("The match date is invalid. It must not be null", DomainErrorType.VALIDATION_ERROR);
    }
}
