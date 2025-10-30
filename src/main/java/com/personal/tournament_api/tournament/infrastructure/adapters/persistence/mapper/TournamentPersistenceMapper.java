package com.personal.tournament_api.tournament.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.infrastructure.adapters.persistence.entity.TournamentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentPersistenceMapper {

    TournamentEntity toEntity(Tournament tournament);

    Tournament toDomain(TournamentEntity entity);

    List<Tournament> toDomainList(List<TournamentEntity> entities);
}