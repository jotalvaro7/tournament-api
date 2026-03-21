package com.personal.tournament_api.auth.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class UserNotFoundException extends AuthDomainException {

    public UserNotFoundException(String email) {
        super("User with email '" + email + "' not found", DomainErrorType.NOT_FOUND);
    }
}