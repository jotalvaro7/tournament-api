package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class DuplicateTournamentNameException extends TournamentDomainException {

    public DuplicateTournamentNameException(String name) {
        super("Tournament with name '" + name + "' already exists", DomainErrorType.RULE_VIOLATION);
    }
}