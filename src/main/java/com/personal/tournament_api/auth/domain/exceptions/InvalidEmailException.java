package com.personal.tournament_api.auth.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidEmailException extends AuthDomainException {

    public InvalidEmailException(String message) {
        super(message, DomainErrorType.VALIDATION_ERROR);
    }
}