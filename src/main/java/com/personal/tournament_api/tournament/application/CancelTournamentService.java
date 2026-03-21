package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.CancelTournamentUseCase;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelTournamentService implements CancelTournamentUseCase {

    private static final Logger log = LoggerFactory.getLogger(CancelTournamentService.class);

    private final TournamentRepository tournamentRepository;

    public CancelTournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public Tournament cancel(Long tournamentId) {
        log.info("Cancelling tournament with id: {}", tournamentId);
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.cancelTournament();
        Tournament cancelled = tournamentRepository.save(tournament);
        log.info("Tournament cancelled with id: {}", tournamentId);
        return cancelled;
    }
}
