package com.personal.tournament_api.team.application;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.player.domain.ports.PlayerRepository;
import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.application.usecases.DeleteTeamUseCase;
import com.personal.tournament_api.team.application.usecases.GetTeamUseCase;
import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.exceptions.TeamHasMatchesException;
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
public class TeamService implements
        CreateTeamUseCase,
        UpdateTeamUseCase,
        GetTeamUseCase,
        DeleteTeamUseCase {

    private final TeamRepository teamRepository;
    private final TeamDomainService teamDomainService;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;


    @Override
    public Team create(CreateTeamCommand command) {
        log.info("Creating team with name: {}", command.name());
        teamDomainService.validateUniqueTeamName(command.name(), teamRepository);
        Team team = new Team(null, command.name(), command.coach(), command.tournamentId());
        Team teamSaved = teamRepository.save(team);
        log.info("Team created with id: {}", teamSaved.getId());
        return teamSaved;
    }

    @Override
    public Team update(UpdateTeamCommand command) {
        log.info("Updating team with id: {}", command.id());
        teamDomainService.validateUniqueNameForUpdate(command.name(), command.id(), teamRepository);
        Team teamFromDb = teamRepository.findById(command.id())
                .orElseThrow(() -> new TeamNotFoundException(command.id()));
        teamFromDb.updateDetails(command.name(), command.coach());
        teamRepository.save(teamFromDb);
        log.info("Team updated with id: {}", teamFromDb.getId());
        return teamFromDb;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> getById(Long teamId) {
        log.info("Fetching team with id: {}", teamId);
        return teamRepository.findById(teamId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllByTournamentIdOrderByNameAsc(Long tournamentId) {
        log.info("Fetching all teams for tournament with id: {}", tournamentId);
        return teamRepository.findAllByTournamentIdOrderByNameAsc(tournamentId);
    }

    @Override
    public void delete(Long teamId) {
        log.info("Deleting team with id: {}", teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        List<Match> associatedMatches = matchRepository.findAllByTeamId(teamId);
        team.validateCanBeDeleted(associatedMatches.size());

        playerRepository.deleteAllByTeamId(teamId);
        log.info("Deleted all players for team with id: {}", teamId);

        teamRepository.deleteById(team.getId());
        log.info("Team deleted with id: {}", teamId);
    }
}
