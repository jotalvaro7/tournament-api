package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.shared.domain.exceptions.DomainException;

public class TeamDomainException extends DomainException {
    public TeamDomainException(String message, DomainErrorType errorType) {
        super(message, errorType);
    }
}
