package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.UpdateMatchUseCase;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UpdateMatchService implements UpdateMatchUseCase {

    private final MatchRepository matchRepository;

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
