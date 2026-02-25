package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.CreateMatchUseCase;
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
public class CreateMatchService implements CreateMatchUseCase {

    private final MatchRepository matchRepository;

    @Override
    public Match create(CreateMatchCommand command) {
        log.info("Creating match between teams {} and {} for tournament {}",
                command.homeTeamId(), command.awayTeamId(), command.tournamentId());

        Match match = new Match(
                null,
                command.tournamentId(),
                command.homeTeamId(),
                command.awayTeamId(),
                command.matchDate(),
                command.field()
        );

        Match savedMatch = matchRepository.save(match);
        log.info("Match created with id: {}", savedMatch.getId());
        return savedMatch;
    }
}
