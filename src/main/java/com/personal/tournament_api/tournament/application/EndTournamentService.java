package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.EndTournamentUseCase;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndTournamentService implements EndTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(EndTournamentService.class);

    private final TournamentRepository tournamentRepository;

    public EndTournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Tournament end(Long tournamentId) {
        log.info("Ending tournament with id: {}", tournamentId);
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.endTournament();
        Tournament ended = tournamentRepository.save(tournament);
        log.info("Tournament ended with id: {}", tournamentId);
        return ended;
    }
}
