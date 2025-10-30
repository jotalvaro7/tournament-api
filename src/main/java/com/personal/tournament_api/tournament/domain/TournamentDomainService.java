package com.personal.tournament_api.tournament.domain;

import com.personal.tournament_api.tournament.domain.exceptions.DuplicateTournamentNameException;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;

public class TournamentDomainService {

    public void validateUniqueName(String name, TournamentRepository repository) {
        if (repository.existsByName(name)) {
            throw new DuplicateTournamentNameException(name);
        }
    }

    public void validateUniqueNameForUpdate(String name, Long tournamentId, TournamentRepository repository) {
        if (repository.existsByNameAndIdNot(name, tournamentId)) {
            throw new DuplicateTournamentNameException(name);
        }
    }
}