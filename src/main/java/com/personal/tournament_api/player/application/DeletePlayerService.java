package com.personal.tournament_api.player.application;

import com.personal.tournament_api.player.application.usecases.DeletePlayerUseCase;
import com.personal.tournament_api.player.domain.exceptions.PlayerNotFoundException;
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
public class DeletePlayerService implements DeletePlayerUseCase {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    public void deletePlayer(Long tournamentId, Long teamId, Long playerId) {
        log.info("Deleting player with id: {} for team id: {}", playerId, teamId);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        team.ensureBelongsToTournament(tournamentId);

        playerRepository.findByIdAndTeamId(playerId, teamId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        playerRepository.deleteById(playerId);
        log.info("Player deleted with id: {}", playerId);
    }
}
