package com.personal.tournament_api.player.domain.ports;

import com.personal.tournament_api.player.domain.model.Player;

import java.util.List;


public interface PlayerRepository {

    Player save(Player player);

    boolean existsByIdentificationNumber(String identificationNumber);

    List<Player> findAllByTeamId(Long teamId);

}
