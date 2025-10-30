package com.personal.tournament_api.tournament.application.usecases;

import com.personal.tournament_api.tournament.domain.model.Tournament;

public interface CreateTournamentUseCase {

    Tournament create(CreateTournamentCommand command);

    record CreateTournamentCommand(
            String name,
            String description
    ) {}
}