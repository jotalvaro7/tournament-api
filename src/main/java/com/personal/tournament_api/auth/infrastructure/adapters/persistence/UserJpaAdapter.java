package com.personal.tournament_api.auth.infrastructure.adapters.persistence;

import com.personal.tournament_api.auth.domain.model.User;
import com.personal.tournament_api.auth.domain.ports.UserRepository;
import com.personal.tournament_api.auth.infrastructure.adapters.persistence.entity.UserEntity;
import com.personal.tournament_api.auth.infrastructure.adapters.persistence.mapper.UserPersistenceMapper;
import com.personal.tournament_api.auth.infrastructure.adapters.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}