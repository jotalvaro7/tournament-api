package com.personal.tournament_api.team.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.team.infrastructure.adapters.persistence.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<TeamEntity> findAllByOrderByNameAsc();

}
