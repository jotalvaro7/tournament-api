package com.personal.tournament_api.auth.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.auth.infrastructure.adapters.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}