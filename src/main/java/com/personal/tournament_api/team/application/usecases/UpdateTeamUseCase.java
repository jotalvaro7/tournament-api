package com.personal.tournament_api.team.application.usecases;

import com.personal.tournament_api.team.domain.model.Team;

public interface UpdateTeamUseCase {

    Team update(Long id, UpdateTeamCommand command);

    record UpdateTeamCommand(
            Long id,
            String name,
            String coach
    ) {}
}
