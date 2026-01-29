package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TeamNotInTournamentException extends TeamDomainException {
    public TeamNotInTournamentException(Long teamId, Long tournamentId) {
        super("Team with id " + teamId + " does not belong to tournament with id " + tournamentId,
                DomainErrorType.VALIDATION_ERROR);
    }
}
