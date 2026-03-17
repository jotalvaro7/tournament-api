package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.StartTournamentUseCase;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class StartTournamentService implements StartTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(StartTournamentService.class);

    private final TournamentRepository tournamentRepository;

    public StartTournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Tournament start(Long tournamentId) {
        log.info("Starting tournament with id: {}", tournamentId);
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.startTournament();
        Tournament started = tournamentRepository.save(tournament);
        log.info("Tournament started with id: {}", tournamentId);
        return started;
    }
}
