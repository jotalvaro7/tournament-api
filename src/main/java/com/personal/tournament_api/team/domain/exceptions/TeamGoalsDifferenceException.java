package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TeamGoalsDifferenceException extends TeamDomainException {
    public TeamGoalsDifferenceException() {
        super("Inconsistent goals difference", DomainErrorType.RULE_VIOLATION);
    }
}
