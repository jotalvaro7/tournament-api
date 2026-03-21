package com.personal.tournament_api.auth.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class DuplicateEmailException extends AuthDomainException {

    public DuplicateEmailException(String email) {
        super("Email '" + email + "' is already registered", DomainErrorType.DUPLICATE_ENTITY);
    }
}