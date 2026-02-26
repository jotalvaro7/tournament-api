package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.GetTeamUseCase;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class GetTeamService implements GetTeamUseCase {

    private final TeamRepository teamRepository;

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
}
