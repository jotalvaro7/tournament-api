package com.personal.tournament_api.auth.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.auth.domain.model.User;
import com.personal.tournament_api.auth.infrastructure.adapters.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPersistenceMapper {

    UserEntity toEntity(User user);

    default User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole()
        );
    }
}