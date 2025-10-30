package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TournamentCannotBeDeletedException extends TournamentDomainException{
    public TournamentCannotBeDeletedException(){
        super("Tournament cannot be deleted in its current state", DomainErrorType.RULE_VIOLATION);
    }

}
