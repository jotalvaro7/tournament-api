package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.DeleteMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteMatchService implements DeleteMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteMatchService.class);

    private final MatchRepository matchRepository;
    private final MatchTeamPort matchTeamPort;
    private final MatchResultService matchResultService;

    public DeleteMatchService(MatchRepository matchRepository, MatchTeamPort matchTeamPort,
                              MatchResultService matchResultService) {
        this.matchRepository = matchRepository;
        this.matchTeamPort = matchTeamPort;
        this.matchResultService = matchResultService;
    }

    @Override
    public void delete(Long matchId) {
        log.info("Deleting match with id: {}", matchId);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        matchResultService.prepareMatchForDeletion(match, matchTeamPort);

        matchRepository.deleteById(match.getId());

        log.info("Match deleted with id: {}", matchId);
    }
}
