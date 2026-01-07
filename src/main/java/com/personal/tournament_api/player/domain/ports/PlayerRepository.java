package com.personal.tournament_api.player.domain.ports;

import com.personal.tournament_api.player.domain.model.Player;

import java.util.List;
import java.util.Optional;


public interface PlayerRepository {

    Player save(Player player);

    boolean existsByIdentificationNumber(String identificationNumber);

    List<Player> findAllByTeamId(Long teamId);

    Optional<Player> findByIdAndTeamId(Long playerId, Long teamId);

    void deleteById(Long playerId);

}
