package com.personal.tournament_api.player.infrastructure.config;

import com.personal.tournament_api.player.application.*;
import com.personal.tournament_api.player.application.usecases.*;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class PlayerModuleConfiguration {

    // --- Application Use Cases ---

    @Bean
    public CreatePlayerUseCase createPlayerUseCase(PlayerRepository playerRepository,
                                                   TeamRepository teamRepository) {
        return new CreatePlayerAdapter(new CreatePlayerService(playerRepository, teamRepository));
    }

    @Bean
    public GetPlayerService getPlayerService(PlayerRepository playerRepository,
                                             TeamRepository teamRepository) {
        return new GetPlayerService(playerRepository, teamRepository);
    }

    @Bean
    public UpdatePlayerUseCase updatePlayerUseCase(PlayerRepository playerRepository,
                                                   TeamRepository teamRepository) {
        return new UpdatePlayerAdapter(new UpdatePlayerService(playerRepository, teamRepository));
    }

    @Bean
    public DeletePlayerUseCase deletePlayerUseCase(PlayerRepository playerRepository,
                                                   TeamRepository teamRepository) {
        return new DeletePlayerAdapter(new DeletePlayerService(playerRepository, teamRepository));
    }

    // --- Transactional Adapters (infrastructure concern) ---

    static class CreatePlayerAdapter implements CreatePlayerUseCase {
        private final CreatePlayerUseCase delegate;

        CreatePlayerAdapter(CreatePlayerUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Player execute(CreatePlayerCommand command) {
            return delegate.execute(command);
        }
    }

    static class UpdatePlayerAdapter implements UpdatePlayerUseCase {
        private final UpdatePlayerUseCase delegate;

        UpdatePlayerAdapter(UpdatePlayerUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Player update(UpdatePlayerCommand command) {
            return delegate.update(command);
        }
    }

    static class DeletePlayerAdapter implements DeletePlayerUseCase {
        private final DeletePlayerUseCase delegate;

        DeletePlayerAdapter(DeletePlayerUseCase delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public void deletePlayer(Long tournamentId, Long teamId, Long playerId) {
            delegate.deletePlayer(tournamentId, teamId, playerId);
        }
    }
}