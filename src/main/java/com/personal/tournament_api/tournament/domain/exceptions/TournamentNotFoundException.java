package com.personal.tournament_api.tournament.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class TournamentNotFoundException extends TournamentDomainException {

    public TournamentNotFoundException(Long id) {
        super("Tournament with id '" + id + "' not found", DomainErrorType.NOT_FOUND);
    }
}