package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTeamNameException extends TeamDomainException{
    public InvalidTeamNameException(){
        super("The team name is invalid. The team name must be between 3 and 100 characters long", DomainErrorType.VALIDATION_ERROR);
    }
}
