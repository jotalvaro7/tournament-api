package com.personal.tournament_api.team.application.usecases;

import com.personal.tournament_api.team.domain.model.Team;

public interface CreateTeamUseCase {

    Team create(CreateTeamCommand command);

    record CreateTeamCommand(
            String name,
            String coach,
            Long tournamentId
    ) {}
}
