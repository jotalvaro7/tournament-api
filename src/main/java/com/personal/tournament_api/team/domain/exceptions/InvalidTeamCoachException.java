package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidTeamCoachException extends TeamDomainException{
    public InvalidTeamCoachException(){
        super("The team coach is invalid. The team coach must be between 3 and 100 characters long", DomainErrorType.VALIDATION_ERROR);
    }
}
