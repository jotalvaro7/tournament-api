package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class CreateTeamService implements CreateTeamUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTeamService.class);

    private final TeamRepository teamRepository;
    private final TeamDomainService teamDomainService;

    public CreateTeamService(TeamRepository teamRepository, TeamDomainService teamDomainService) {
        this.teamRepository = teamRepository;
        this.teamDomainService = teamDomainService;
    }

    @Override
    public Team create(CreateTeamCommand command) {
        log.info("Creating team with name: {}", command.name());
        teamDomainService.validateUniqueTeamName(command.name(), teamRepository);
        Team team = new Team(null, command.name(), command.coach(), command.tournamentId());
        Team teamSaved = teamRepository.save(team);
        log.info("Team created with id: {}", teamSaved.getId());
        return teamSaved;
    }
}
