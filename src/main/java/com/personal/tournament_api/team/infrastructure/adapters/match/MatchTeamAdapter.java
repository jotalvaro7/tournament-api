package com.personal.tournament_api.team.infrastructure.adapters.match;

import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchTeamAdapter implements MatchTeamPort {

    private final TeamRepository teamRepository;

    @Override
    public Optional<Team> findById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }
}
