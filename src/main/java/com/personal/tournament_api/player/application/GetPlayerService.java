package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.application.usecases.GetPlayerByIdUseCase;
import com.personal.tournament_api.player.application.usecases.GetPlayersByTeamUseCase;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class GetPlayerService implements GetPlayersByTeamUseCase, GetPlayerByIdUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetPlayerService.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public GetPlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public List<Player> getPlayersByTeamId(Long teamId) {
        log.info("Fetching players for team id: {}", teamId);
        teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        List<Player> players = playerRepository.findAllByTeamId(teamId);
        log.info("Found {} players for team id: {}", players.size(), teamId);
        return players;
    }

    @Override
    public Optional<Player> getPlayerById(Long teamId, Long playerId) {
        log.info("Fetching player with id: {} for team id: {}", playerId, teamId);
        teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        Optional<Player> player = playerRepository.findByIdAndTeamId(playerId, teamId);
        log.info("Player found: {}", player.isPresent());
        return player;
    }
}
