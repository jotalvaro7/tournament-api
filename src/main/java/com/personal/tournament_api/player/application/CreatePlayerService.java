package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.application.usecases.CreatePlayerUseCase;
import com.personal.tournament_api.player.domain.exceptions.DuplicatePlayerIdentificationException;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class CreatePlayerService implements CreatePlayerUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreatePlayerService.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public CreatePlayerService(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

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
}
