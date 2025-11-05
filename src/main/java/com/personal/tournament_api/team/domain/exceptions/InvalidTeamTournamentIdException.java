package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTeamTournamentIdException extends TeamDomainException{
    public InvalidTeamTournamentIdException() {
        super("Invalid team tournament ID. The tournament ID must be a positive number.", DomainErrorType.RULE_VIOLATION);
    }
}
