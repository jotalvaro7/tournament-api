package com.personal.tournament_api.application.ports.in.tournament;

import com.personal.tournament_api.domain.model.Tournament;

import java.util.List;
import java.util.Optional;

public interface GetTournamentUseCase {

    Optional<Tournament> getById(Long tournamentId);

    List<Tournament> getAll();
}