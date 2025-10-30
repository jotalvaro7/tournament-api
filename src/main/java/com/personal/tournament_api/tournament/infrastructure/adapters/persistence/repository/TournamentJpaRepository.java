package com.personal.tournament_api.tournament.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.tournament.infrastructure.adapters.persistence.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentJpaRepository extends JpaRepository<TournamentEntity, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}