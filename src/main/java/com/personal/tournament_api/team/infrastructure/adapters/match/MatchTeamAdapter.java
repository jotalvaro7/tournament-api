package com.personal.tournament_api.team.infrastructure.adapters.match;

import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchTeamAdapter implements MatchTeamPort {

    private final TeamRepository teamRepository;

    @Override
    public void validateBelongsToTournament(Long teamId, Long tournamentId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        team.ensureBelongsToTournament(tournamentId);
    }

    @Override
    public void recordMatchResult(Long teamId, int goalsFor, int goalsAgainst) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        team.recordMatchResult(goalsFor, goalsAgainst);
        teamRepository.save(team);
    }

    @Override
    public void reverseMatchResult(Long teamId, int goalsFor, int goalsAgainst) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        team.reverseMatchResult(goalsFor, goalsAgainst);
        teamRepository.save(team);
    }
}
