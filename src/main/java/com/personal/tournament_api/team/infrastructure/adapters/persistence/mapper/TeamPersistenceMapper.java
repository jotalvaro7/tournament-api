package com.personal.tournament_api.team.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.entity.TeamEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TeamPersistenceMapper {

    public abstract TeamEntity toEntity(Team team);

    public abstract List<Team> toDomainList(List<TeamEntity> entities);

    public Team toDomain(TeamEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Team(
            entity.getId(),
            entity.getName(),
            entity.getCoach(),
            entity.getTournamentId(),
            entity.getPoints(),
            entity.getMatchesPlayed(),
            entity.getMatchesWin(),
            entity.getMatchesDraw(),
            entity.getMatchesLost(),
            entity.getGoalsFor(),
            entity.getGoalsAgainst(),
            entity.getGoalDifference()
        );
    }
}
