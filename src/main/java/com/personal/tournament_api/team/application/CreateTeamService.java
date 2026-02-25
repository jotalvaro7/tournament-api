package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.domain.TeamDomainService;
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
public class CreateTeamService implements CreateTeamUseCase {

    private final TeamRepository teamRepository;
    private final TeamDomainService teamDomainService;

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
