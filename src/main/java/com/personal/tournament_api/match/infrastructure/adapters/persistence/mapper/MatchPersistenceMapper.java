package com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MatchPersistenceMapper {

    default MatchEntity toEntity(Match match) {
        if (match == null) {
            return null;
        }

        MatchEntity entity = new MatchEntity();
        entity.setId(match.getId());
        entity.setTournamentId(match.getTournamentId());
        entity.setHomeTeamId(match.getHomeTeamId());
        entity.setAwayTeamId(match.getAwayTeamId());
        entity.setHomeTeamScore(match.getHomeTeamScore());
        entity.setAwayTeamScore(match.getAwayTeamScore());
        entity.setMatchDate(match.getMatchDate());
        entity.setField(match.getField());
        entity.setStatus(match.getStatus());

        return entity;
    }

    default Match toDomain(MatchEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Match(
                entity.getId(),
                entity.getTournamentId(),
                entity.getHomeTeamId(),
                entity.getAwayTeamId(),
                entity.getHomeTeamScore(),
                entity.getAwayTeamScore(),
                entity.getMatchDate(),
                entity.getField(),
                entity.getStatus()
        );
    }

    default List<Match> toDomainList(List<MatchEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
