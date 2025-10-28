package com.personal.tournament_api.domain.exceptions;

public class DuplicateTournamentNameException extends TournamentDomainException {

    public DuplicateTournamentNameException(String name) {
        super("Tournament with name '" + name + "' already exists");
    }
}