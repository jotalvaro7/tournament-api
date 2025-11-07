package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchStatusTransitionException extends MatchDomainException {
    public InvalidMatchStatusTransitionException(String message) {
        super(message, DomainErrorType.RULE_VIOLATION);
    }
}
