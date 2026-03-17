package com.personal.tournament_api.player.infrastructure.config;

import com.personal.tournament_api.player.application.*;
import com.personal.tournament_api.player.application.usecases.*;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayerModuleConfiguration {

    @Bean
    public CreatePlayerUseCase createPlayerUseCase(PlayerRepository playerRepository,
                                                   TeamRepository teamRepository) {
        return new CreatePlayerService(playerRepository, teamRepository);
    }

    @Bean
    public GetPlayerService getPlayerService(PlayerRepository playerRepository,
                                             TeamRepository teamRepository) {
        return new GetPlayerService(playerRepository, teamRepository);
    }

    @Bean
    public UpdatePlayerUseCase updatePlayerUseCase(PlayerRepository playerRepository,
                                                   TeamRepository teamRepository) {
        return new UpdatePlayerService(playerRepository, teamRepository);
    }

    @Bean
    public DeletePlayerUseCase deletePlayerUseCase(PlayerRepository playerRepository,
                                                   TeamRepository teamRepository) {
        return new DeletePlayerService(playerRepository, teamRepository);
    }
}
