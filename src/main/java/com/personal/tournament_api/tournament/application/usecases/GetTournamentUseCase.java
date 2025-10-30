package com.personal.tournament_api.tournament.application.usecases;

import com.personal.tournament_api.tournament.domain.model.Tournament;

import java.util.List;
import java.util.Optional;

public interface GetTournamentUseCase {

    Optional<Tournament> getById(Long tournamentId);

    List<Tournament> getAll();
}