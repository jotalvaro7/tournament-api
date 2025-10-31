package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.team.domain.TeamDomainService;

public class DuplicateTeamNameException extends TeamDomainException {
    public DuplicateTeamNameException(String name){
        super("Team with name '" + name + "' already exists in the tournament", DomainErrorType.RULE_VIOLATION);
    }
}
