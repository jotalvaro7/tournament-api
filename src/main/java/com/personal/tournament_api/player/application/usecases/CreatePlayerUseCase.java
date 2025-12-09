package com.personal.tournament_api.player.application.usecases;

import com.personal.tournament_api.player.domain.model.Player;

public interface CreatePlayerUseCase {

    record CreatePlayerCommand(
            String name,
            String lastName,
            String identificationNumber,
            Long teamId,
            Long tournamentId
    ) {
    }

    Player execute(CreatePlayerCommand command);
}
