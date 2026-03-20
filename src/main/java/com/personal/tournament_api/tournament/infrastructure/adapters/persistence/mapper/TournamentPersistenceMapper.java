package com.personal.tournament_api.tournament.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.infrastructure.adapters.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentPersistenceMapper {

    TournamentEntity toEntity(Tournament tournament);

    default Tournament toDomain(TournamentEntity entity) {
        if (entity == null) return null;
        return Tournament.reconstitute(entity.getId(), entity.getName(), entity.getDescription(), entity.getStatus());
    }

    default List<Tournament> toDomainList(List<TournamentEntity> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDomain).toList();
    }
}