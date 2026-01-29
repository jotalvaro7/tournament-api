package com.personal.tournament_api.player.application.usecases;

import com.personal.tournament_api.player.domain.model.Player;

public interface UpdatePlayerUseCase {
    record UpdatePlayerCommand(
            Long playerId,
            Long teamId,
            Long tournamentId,
            String name,
            String lastName,
            String identificationNumber
    ) {
    }

    Player update(UpdatePlayerCommand command);
}
