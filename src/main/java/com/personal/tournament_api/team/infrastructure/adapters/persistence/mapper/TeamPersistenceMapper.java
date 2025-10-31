package com.personal.tournament_api.team.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamPersistenceMapper {

    TeamEntity toEntity(Team team);

    Team toDomain(TeamEntity entity);

    List<Team> toDomainList(List<TeamEntity> entities);
}
