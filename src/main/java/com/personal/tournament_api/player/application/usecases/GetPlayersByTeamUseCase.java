package com.personal.tournament_api.player.application.usecases;

import com.personal.tournament_api.player.domain.model.Player;

import java.util.List;

public interface GetPlayersByTeamUseCase {
    List<Player> getPlayersByTeamId(Long teamId);
}
