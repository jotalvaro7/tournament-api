package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TeamHasMatchesException extends TeamDomainException {
    public TeamHasMatchesException(Long teamId, int matchCount) {
        super(String.format("Team with id %d cannot be deleted because it has %d associated match(es). Delete the matches first.",
              teamId, matchCount),
              DomainErrorType.CONFLICT);
    }
}
