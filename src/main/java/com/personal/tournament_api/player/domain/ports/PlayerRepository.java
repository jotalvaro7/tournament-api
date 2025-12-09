package com.personal.tournament_api.player.domain.ports;

import com.personal.tournament_api.player.domain.model.Player;


public interface PlayerRepository {

    Player save(Player player);

    boolean existsByIdentificationNumber(String identificationNumber);

}
