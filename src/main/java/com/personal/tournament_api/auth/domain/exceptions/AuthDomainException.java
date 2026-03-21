package com.personal.tournament_api.auth.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.shared.domain.exceptions.DomainException;

public class AuthDomainException extends DomainException {

    public AuthDomainException(String message, DomainErrorType errorType) {
        super(message, errorType);
    }
}