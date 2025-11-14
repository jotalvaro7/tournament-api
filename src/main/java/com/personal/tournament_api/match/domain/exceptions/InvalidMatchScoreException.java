package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidMatchScoreException extends MatchDomainException {
    public InvalidMatchScoreException() {
        super("The match score is invalid. Scores cannot be negative", DomainErrorType.VALIDATION_ERROR);
    }
}
