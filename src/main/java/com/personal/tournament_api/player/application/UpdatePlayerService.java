package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.application.usecases.UpdatePlayerUseCase;
import com.personal.tournament_api.player.domain.exceptions.DuplicatePlayerIdentificationException;
import com.personal.tournament_api.player.domain.exceptions.PlayerNotFoundException;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class UpdatePlayerService implements UpdatePlayerUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdatePlayerService.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public UpdatePlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Player update(UpdatePlayerCommand command) {
        log.info("Updating player with id: {} for team id: {}", command.playerId(), command.teamId());

        Team team = teamRepository.findById(command.teamId())
                .orElseThrow(() -> new TeamNotFoundException(command.teamId()));
        team.ensureBelongsToTournament(command.tournamentId());

        Player player = playerRepository.findByIdAndTeamId(command.playerId(), command.teamId())
                .orElseThrow(() -> new PlayerNotFoundException(command.playerId()));

        boolean identificationChanged = !player.getIdentificationNumber().equals(command.identificationNumber());
        if (identificationChanged && playerRepository.existsByIdentificationNumber(command.identificationNumber())) {
            throw new DuplicatePlayerIdentificationException(command.identificationNumber());
        }

        player.updateDetails(command.name(), command.lastName(), command.identificationNumber());
        Player updatedPlayer = playerRepository.save(player);
        log.info("Player updated with id: {}", updatedPlayer.getId());
        return updatedPlayer;
    }
}
