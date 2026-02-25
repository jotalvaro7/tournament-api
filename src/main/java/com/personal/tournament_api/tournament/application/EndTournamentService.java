package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.EndTournamentUseCase;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EndTournamentService implements EndTournamentUseCase {

    private final TournamentRepository tournamentRepository;

    @Override
    public Tournament end(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new TournamentNotFoundException(tournamentId));
        tournament.endTournament();
        return tournamentRepository.save(tournament);
    }
}
