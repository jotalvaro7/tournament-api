package com.personal.tournament_api.application.ports.in.tournament;

import com.personal.tournament_api.domain.model.Tournament;

public interface UpdateTournamentUseCase {

    Tournament update(UpdateTournamentCommand command);

    record UpdateTournamentCommand(
            Long id,
            String name,
            String description
    ) {}
}