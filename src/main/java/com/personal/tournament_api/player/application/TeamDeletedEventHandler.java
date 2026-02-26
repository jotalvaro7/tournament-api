package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.events.TeamDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
