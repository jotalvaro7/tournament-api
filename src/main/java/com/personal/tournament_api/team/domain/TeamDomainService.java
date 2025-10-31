package com.personal.tournament_api.team.domain;

import com.personal.tournament_api.team.domain.exceptions.DuplicateTeamNameException;
import com.personal.tournament_api.team.domain.ports.TeamRepository;

public class TeamDomainService {

    public void validateUniqueTeamName(String name, TeamRepository repository) {
        if(repository.existsByName(name)) {
            throw new DuplicateTeamNameException(name);
        }
    }

    public void validateUniqueNameForUpdate(String name, Long teamId, TeamRepository repository) {
        if (repository.existsByNameAndIdNot(name, teamId)) {
            throw new DuplicateTeamNameException(name);
        }
    }
}
