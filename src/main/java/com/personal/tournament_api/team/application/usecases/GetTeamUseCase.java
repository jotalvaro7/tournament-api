package com.personal.tournament_api.team.application.usecases;

import com.personal.tournament_api.team.domain.model.Team;

import java.util.List;
import java.util.Optional;

public interface GetTeamUseCase {

    Optional<Team> getById(Long teamId);

    List<Team> getAll();
}
