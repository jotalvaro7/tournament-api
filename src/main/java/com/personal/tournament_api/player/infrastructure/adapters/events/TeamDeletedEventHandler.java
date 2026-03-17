package com.personal.tournament_api.player.infrastructure.adapters.events;

import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.events.TeamDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada (Spring Events → Application).
 * Traduce el evento TeamDeletedEvent al puerto de salida PlayerRepository.
 * Vive en infrastructure porque usa Spring (@Component, @EventListener).
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TeamDeletedEventHandler {

    private final PlayerRepository playerRepository;

    @EventListener
    public void handle(TeamDeletedEvent event) {
        log.info("Handling TeamDeletedEvent: deleting players for team with id: {}", event.teamId());
        playerRepository.deleteAllByTeamId(event.teamId());
        log.info("Deleted all players for team with id: {}", event.teamId());
    }
}
