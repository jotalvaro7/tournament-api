package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchFieldException extends MatchDomainException {
    public InvalidMatchFieldException() {
        super("The field name is invalid. It must not be empty and cannot exceed 100 characters", DomainErrorType.VALIDATION_ERROR);
    }
}
