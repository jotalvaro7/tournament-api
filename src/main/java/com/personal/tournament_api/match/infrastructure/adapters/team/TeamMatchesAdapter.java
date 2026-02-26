package com.personal.tournament_api.match.infrastructure.adapters.team;

import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.team.domain.ports.TeamMatchesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMatchesAdapter implements TeamMatchesPort {

    private final MatchRepository matchRepository;

    @Override
    public int countByTeamId(Long teamId) {
        return matchRepository.findAllByTeamId(teamId).size();
    }
}
