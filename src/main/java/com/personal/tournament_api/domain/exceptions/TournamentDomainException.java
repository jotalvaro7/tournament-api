package com.personal.tournament_api.domain.exceptions;

public class TournamentDomainException extends RuntimeException {
    public TournamentDomainException(String message) {
        super(message);
    }
}
