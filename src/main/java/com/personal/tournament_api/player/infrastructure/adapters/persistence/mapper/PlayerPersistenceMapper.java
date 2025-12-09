package com.personal.tournament_api.player.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.infrastructure.adapters.persistence.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerPersistenceMapper {

    default Player toDomain(PlayerEntity entity) {
        if (entity == null) {
            return null;
        }
        return Player.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getIdentificationNumber(),
                entity.getTeamId()
        );
    }

    PlayerEntity toEntity(Player player);

    default List<Player> toDomainList(List<PlayerEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}
