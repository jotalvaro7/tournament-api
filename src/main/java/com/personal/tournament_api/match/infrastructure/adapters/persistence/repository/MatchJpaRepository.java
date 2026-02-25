package com.personal.tournament_api.match.infrastructure.adapters.persistence.repository;

import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchJpaRepository extends JpaRepository<MatchEntity, Long>, JpaSpecificationExecutor<MatchEntity> {

    List<MatchEntity> findAllByTournamentId(Long tournamentId);

    //@Query(value = "SELECT * FROM matches WHERE home_team_id = :teamId OR away_team_id = :teamId ORDER BY match_date ASC", nativeQuery = true)
    @Query("SELECT m FROM MatchEntity m WHERE m.homeTeamId = :teamId OR m.awayTeamId = :teamId ORDER BY m.matchDate DESC")
    List<MatchEntity> findAllByTeamId(@Param("teamId") Long teamId);

    void deleteByTournamentId(Long tournamentId);
}
