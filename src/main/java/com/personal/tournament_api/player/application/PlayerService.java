package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.application.usecases.CreatePlayerUseCase;
import com.personal.tournament_api.player.application.usecases.GetPlayerByIdUseCase;
import com.personal.tournament_api.player.application.usecases.GetPlayersByTeamUseCase;
import com.personal.tournament_api.player.domain.exceptions.DuplicatePlayerIdentificationException;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PlayerService implements
        CreatePlayerUseCase,
        GetPlayersByTeamUseCase,
        GetPlayerByIdUseCase {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    public Player execute(CreatePlayerCommand command) {
        log.info("Creating player with name: {} {} for team: {}",
                command.name(), command.lastName(), command.teamId());

        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new TeamNotFoundException(command.teamId()));

        team.ensureBelongsToTournament(command.tournamentId());

        if (playerRepository.existsByIdentificationNumber(command.identificationNumber())) {
            throw new DuplicatePlayerIdentificationException(command.identificationNumber());
        }

        Player player = Player.create(command.name(), command.lastName(), command.identificationNumber(), command.teamId());

        Player savedPlayer = playerRepository.save(player);
        log.info("Player created with id: {}", savedPlayer.getId());
        return savedPlayer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> getPlayersByTeamId(Long teamId) {
        log.info("Fetching players for team id: {}", teamId);

        teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        List<Player> players = playerRepository.findAllByTeamId(teamId);
        log.info("Found {} players for team id: {}", players.size(), teamId);
        return players;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerById(Long teamId, Long playerId) {
        log.info("Fetching player with id: {} for team id: {}", playerId, teamId);

        teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        Optional<Player> player = playerRepository.findByIdAndTeamId(playerId, teamId);
        log.info("Player found: {}", player.isPresent());
        return player;
    }
}
