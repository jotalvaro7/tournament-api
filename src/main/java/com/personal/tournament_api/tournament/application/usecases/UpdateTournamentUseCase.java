package com.personal.tournament_api.tournament.application.usecases;

import com.personal.tournament_api.tournament.domain.model.Tournament;

public interface UpdateTournamentUseCase {

    Tournament update(UpdateTournamentCommand command);

    record UpdateTournamentCommand(
            Long id,
            String name,
            String description
    ) {}
}