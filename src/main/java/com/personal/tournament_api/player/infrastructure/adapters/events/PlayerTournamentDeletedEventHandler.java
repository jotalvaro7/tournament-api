package com.personal.tournament_api.player.infrastructure.adapters.events;

import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.tournament.domain.events.TournamentDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada (Spring Events → Application).
 * Traduce el evento TournamentDeletedEvent al puerto de salida PlayerRepository.
 * Vive en infrastructure porque usa Spring (@Component, @EventListener, @Order).
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerTournamentDeletedEventHandler {

    private final PlayerRepository playerRepository;

    @EventListener
    @Order(2)
    public void handle(TournamentDeletedEvent event) {
        log.info("Handling TournamentDeletedEvent: deleting players for tournament with id: {}", event.tournamentId());
        playerRepository.deleteAllByTournamentId(event.tournamentId());
        log.info("Deleted all players for tournament with id: {}", event.tournamentId());
    }
}
