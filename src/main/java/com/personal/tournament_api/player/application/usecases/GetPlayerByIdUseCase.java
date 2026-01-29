package com.personal.tournament_api.player.application.usecases;

import com.personal.tournament_api.player.domain.model.Player;

import java.util.Optional;

public interface GetPlayerByIdUseCase {
    Optional<Player> getPlayerById(Long teamId, Long playerId);
}
