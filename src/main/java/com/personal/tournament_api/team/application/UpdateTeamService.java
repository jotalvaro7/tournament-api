package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase;
import com.personal.tournament_api.team.domain.TeamDomainService;
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
public class UpdateTeamService implements UpdateTeamUseCase {

    private final TeamRepository teamRepository;
    private final TeamDomainService teamDomainService;

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
}
