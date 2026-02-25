package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.GetTournamentUseCase;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetTournamentService implements GetTournamentUseCase {

    private final TournamentRepository tournamentRepository;

    @Override
    public Optional<Tournament> getById(Long tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

    @Override
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }
}
