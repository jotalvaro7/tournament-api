package com.personal.tournament_api.player.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.player.infrastructure.adapters.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, Long> {

    boolean existsByIdentificationNumber(String identificationNumber);

    List<PlayerEntity> findAllByTeamId(Long teamId);

    Optional<PlayerEntity> findByIdAndTeamId(Long id, Long teamId);

    void deleteAllByTeamId(Long teamId);

    @Modifying
    @Query("DELETE FROM PlayerEntity p WHERE p.teamId IN (SELECT t.id FROM TeamEntity t WHERE t.tournamentId = :tournamentId)")
    void deleteAllByTournamentId(@Param("tournamentId") Long tournamentId);

}
