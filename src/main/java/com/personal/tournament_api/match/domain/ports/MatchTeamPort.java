package com.personal.tournament_api.match.domain.ports;

import com.personal.tournament_api.team.domain.model.Team;

import java.util.Optional;

public interface MatchTeamPort {

    Optional<Team> findById(Long teamId);

    Team save(Team team);
}
