package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FinishMatchService implements FinishMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(FinishMatchService.class);

    private final MatchRepository matchRepository;
    private final MatchTeamPort matchTeamPort;
    private final MatchResultService matchResultService;

    public FinishMatchService(MatchRepository matchRepository, MatchTeamPort matchTeamPort,
                              MatchResultService matchResultService) {
        this.matchRepository = matchRepository;
        this.matchTeamPort = matchTeamPort;
        this.matchResultService = matchResultService;
    }

    @Override
    public Match finishMatch(FinishMatchCommand command) {
        log.info("Setting result for match with id: {}", command.matchId());

        Match match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new MatchNotFoundException(command.matchId()));

        var outcome = matchResultService.registerResult(match, matchTeamPort, command.homeTeamScore(), command.awayTeamScore());

        matchRepository.save(match);

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
