package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.CreateMatchUseCase;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateMatchService implements CreateMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateMatchService.class);

    private final MatchRepository matchRepository;

    public CreateMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match create(CreateMatchCommand command) {
        log.info("Creating match between teams {} and {} for tournament {}",
                command.homeTeamId(), command.awayTeamId(), command.tournamentId());

        Match match = Match.create(
                command.tournamentId(),
                command.homeTeamId(),
                command.awayTeamId(),
                command.matchDate(),
                command.field(),
                command.matchday()
        );

        Match savedMatch = matchRepository.save(match);
        log.info("Match created with id: {}", savedMatch.getId());
        return savedMatch;
    }
}
