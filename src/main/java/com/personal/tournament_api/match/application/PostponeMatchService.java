package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.PostponeMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostponeMatchService implements PostponeMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(PostponeMatchService.class);

    private final MatchRepository matchRepository;

    public PostponeMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match postponeMatch(Long matchId) {
        log.info("Postponing match with id: {}", matchId);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));

        match.postponeMatch();
        Match postponedMatch = matchRepository.save(match);

        log.info("Match postponed with id: {}", postponedMatch.getId());
        return postponedMatch;
    }
}
