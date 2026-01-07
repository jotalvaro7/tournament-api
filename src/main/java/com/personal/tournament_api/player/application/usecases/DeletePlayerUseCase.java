package com.personal.tournament_api.player.application.usecases;

public interface DeletePlayerUseCase {
    void deletePlayer(Long tournamentId, Long teamId, Long playerId);
}
