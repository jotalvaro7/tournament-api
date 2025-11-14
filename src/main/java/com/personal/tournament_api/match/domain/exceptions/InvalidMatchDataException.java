package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchDataException extends MatchDomainException {
    public InvalidMatchDataException(String message) {
        super(message, DomainErrorType.VALIDATION_ERROR);
    }
}
