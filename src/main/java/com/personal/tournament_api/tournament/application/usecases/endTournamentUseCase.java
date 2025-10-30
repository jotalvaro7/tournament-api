package com.personal.tournament_api.tournament.application.usecases;

import com.personal.tournament_api.tournament.domain.model.Tournament;

public interface endTournamentUseCase {

    Tournament end(Long tournamentId);
}