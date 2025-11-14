package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class MatchNotFoundException extends MatchDomainException {
    public MatchNotFoundException(Long matchId) {
        super("Match with ID " + matchId + " not found", DomainErrorType.NOT_FOUND);
    }
}
