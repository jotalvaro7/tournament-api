package com.personal.tournament_api.shared.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class DomainException extends RuntimeException{

    private final DomainErrorType errorType;

    public DomainException(String message, DomainErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public DomainErrorType getErrorType() {
        return errorType;
    }

}
