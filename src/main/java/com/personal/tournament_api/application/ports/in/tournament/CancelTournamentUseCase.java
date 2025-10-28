package com.personal.tournament_api.application.ports.in.tournament;

import com.personal.tournament_api.domain.model.Tournament;

public interface CancelTournamentUseCase {

    Tournament cancel(Long tournamentId);
}