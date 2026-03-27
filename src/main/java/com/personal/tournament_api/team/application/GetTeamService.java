package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.GetTeamUseCase;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class GetTeamService implements GetTeamUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetTeamService.class);

    private final TeamRepository teamRepository;

    public GetTeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Optional<Team> getById(Long teamId) {
        log.info("Fetching team with id: {}", teamId);
        return teamRepository.findById(teamId);
    }

    @Override
    public List<Team> getAllByTournamentIdOrderByNameAsc(Long tournamentId) {
        log.info("Fetching all teams for tournament with id: {}", tournamentId);
        return teamRepository.findAllByTournamentIdOrderByNameAsc(tournamentId);
    }

    @Override
    public List<Team> getStandingsByTournamentId(Long tournamentId) {
        log.info("Fetching standings for tournament with id: {}", tournamentId);
        return teamRepository.findStandingsByTournamentId(tournamentId);
    }
}
