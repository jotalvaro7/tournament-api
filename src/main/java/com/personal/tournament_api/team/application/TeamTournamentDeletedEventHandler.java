package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.domain.ports.TeamRepository;
import com.personal.tournament_api.tournament.domain.events.TournamentDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamTournamentDeletedEventHandler {

    private final TeamRepository teamRepository;

    @EventListener
    @Order(3)
    public void handle(TournamentDeletedEvent event) {
        log.info("Handling TournamentDeletedEvent: deleting teams for tournament with id: {}", event.tournamentId());
        teamRepository.deleteByTournamentId(event.tournamentId());
        log.info("Deleted all teams for tournament with id: {}", event.tournamentId());
    }
}
