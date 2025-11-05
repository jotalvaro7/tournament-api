package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TeamMatchesPlayedException extends TeamDomainException{
    public TeamMatchesPlayedException() {
        super("Inconsistent matches played", DomainErrorType.RULE_VIOLATION);
    }
}
