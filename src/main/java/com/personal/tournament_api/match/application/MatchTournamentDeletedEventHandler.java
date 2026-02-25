package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.tournament.domain.events.TournamentDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchTournamentDeletedEventHandler {

    private final MatchRepository matchRepository;

    @EventListener
    @Order(1)
    public void handle(TournamentDeletedEvent event) {
        log.info("Handling TournamentDeletedEvent: deleting matches for tournament with id: {}", event.tournamentId());
        matchRepository.deleteByTournamentId(event.tournamentId());
        log.info("Deleted all matches for tournament with id: {}", event.tournamentId());
    }
}
