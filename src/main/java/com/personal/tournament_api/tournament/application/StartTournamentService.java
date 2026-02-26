package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.StartTournamentUseCase;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StartTournamentService implements StartTournamentUseCase {

    private final TournamentRepository tournamentRepository;

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
