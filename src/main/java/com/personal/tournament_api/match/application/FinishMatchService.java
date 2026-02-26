package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase;
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
public class FinishMatchService implements FinishMatchUseCase {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchResultService matchResultService;

    @Override
    public Match finishMatch(FinishMatchCommand command) {
        log.info("Setting result for match with id: {}", command.matchId());

        Match match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new MatchNotFoundException(command.matchId()));

        Team homeTeam = teamRepository.findById(match.getHomeTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getHomeTeamId()));
        Team awayTeam = teamRepository.findById(match.getAwayTeamId())
                .orElseThrow(() -> new TeamNotFoundException(match.getAwayTeamId()));

        var outcome = matchResultService.registerResult(match, homeTeam, awayTeam, command.homeTeamScore(), command.awayTeamScore());

        matchRepository.save(match);
        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);

        if (outcome.isCorrection()) {
            log.warn("Match {} result CORRECTED from {}-{} to {}-{}. Teams statistics updated.",
                    match.getId(), outcome.previousHomeScore(), outcome.previousAwayScore(),
                    command.homeTeamScore(), command.awayTeamScore());
        } else {
            log.info("Match {} result SET to {}-{}. Teams statistics updated.",
                    match.getId(), command.homeTeamScore(), command.awayTeamScore());
        }

        return match;
    }
}
