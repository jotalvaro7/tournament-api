package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.PostponeMatchUseCase;
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
public class PostponeMatchService implements PostponeMatchUseCase {

    private final MatchRepository matchRepository;

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
