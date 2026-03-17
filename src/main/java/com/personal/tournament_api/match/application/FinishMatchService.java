package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class FinishMatchService implements FinishMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(FinishMatchService.class);

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchResultService matchResultService;

    public FinishMatchService(MatchRepository matchRepository, TeamRepository teamRepository,
                              MatchResultService matchResultService) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.matchResultService = matchResultService;
    }

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
