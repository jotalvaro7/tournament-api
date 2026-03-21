package com.personal.tournament_api.auth.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidCredentialsException extends AuthDomainException {

    public InvalidCredentialsException() {
        super("Invalid email or password", DomainErrorType.UNAUTHORIZED);
    }
}