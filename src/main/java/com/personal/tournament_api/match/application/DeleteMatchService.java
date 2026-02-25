package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.DeleteMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DeleteMatchService implements DeleteMatchUseCase {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchResultService matchResultService;

    @Override
    public void delete(Long matchId) {
        log.info("Deleting match with id: {}", matchId);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        Team homeTeam = teamRepository.findById(match.getHomeTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getHomeTeamId()));
        Team awayTeam = teamRepository.findById(match.getAwayTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getAwayTeamId()));

        matchResultService.prepareMatchForDeletion(match, homeTeam, awayTeam);

        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);
        matchRepository.deleteById(match.getId());

        log.info("Match deleted with id: {}", matchId);
    }
}
