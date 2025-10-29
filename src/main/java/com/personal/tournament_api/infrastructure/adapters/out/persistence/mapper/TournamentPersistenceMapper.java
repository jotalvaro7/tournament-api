package com.personal.tournament_api.infrastructure.adapters.out.persistence.mapper;

import com.personal.tournament_api.domain.model.Tournament;
import com.personal.tournament_api.infrastructure.adapters.out.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentPersistenceMapper {

    TournamentEntity toEntity(Tournament tournament);

    Tournament toDomain(TournamentEntity entity);

    List<Tournament> toDomainList(List<TournamentEntity> entities);
}