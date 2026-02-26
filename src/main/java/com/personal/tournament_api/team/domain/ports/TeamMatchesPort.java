package com.personal.tournament_api.team.domain.ports;

public interface TeamMatchesPort {

    int countByTeamId(Long teamId);
}
