package com.personal.tournament_api.application.ports.in.tournament;

import com.personal.tournament_api.domain.model.Tournament;

public interface CreateTournamentUseCase {

    Tournament create(CreateTournamentCommand command);

    record CreateTournamentCommand(
            String name,
            String description
    ) {}
}