package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.GetTournamentUseCase;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class GetTournamentService implements GetTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetTournamentService.class);

    private final TournamentRepository tournamentRepository;

    public GetTournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Optional<Tournament> getById(Long tournamentId) {
        log.info("Fetching tournament with id: {}", tournamentId);
        return tournamentRepository.findById(tournamentId);
    }

    @Override
    public List<Tournament> getAll() {
        log.info("Fetching all tournaments");
        return tournamentRepository.findAll();
    }
}
