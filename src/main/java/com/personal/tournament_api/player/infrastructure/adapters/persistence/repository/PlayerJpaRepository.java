package com.personal.tournament_api.player.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.player.infrastructure.adapters.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, Long> {

    boolean existsByIdentificationNumber(String identificationNumber);

    List<PlayerEntity> findAllByTeamId(Long teamId);

}
