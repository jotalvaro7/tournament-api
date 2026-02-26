package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.application.usecases.CreatePlayerUseCase;
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

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CreatePlayerService implements CreatePlayerUseCase {

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
}
