package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.UpdateMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class UpdateMatchService implements UpdateMatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateMatchService.class);

    private final MatchRepository matchRepository;

    public UpdateMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Match update(UpdateMatchCommand command) {
        log.info("Updating match with id: {}", command.matchId());

        Match match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new MatchNotFoundException(command.matchId()));

        match.updateMatchDetails(command.matchDate(), command.field());
        Match updatedMatch = matchRepository.save(match);

        log.info("Match updated with id: {}", updatedMatch.getId());
        return updatedMatch;
    }
}
