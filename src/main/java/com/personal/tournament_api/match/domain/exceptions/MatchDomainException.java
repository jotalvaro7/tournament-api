package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.shared.domain.exceptions.DomainException;

public class MatchDomainException extends DomainException {
    public MatchDomainException(String message, DomainErrorType errorType) {
        super(message, errorType);
    }
}
