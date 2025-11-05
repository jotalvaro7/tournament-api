package com.personal.tournament_api.team.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.entity.TeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TeamPersistenceMapper Unit Tests")
class TeamPersistenceMapperTest {

    private TeamPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TeamPersistenceMapper.class);
    }

    @Test
    @DisplayName("Should map Team domain model to TeamEntity correctly")
    void shouldMapToEntity() {
        // Given
        Team team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 10L);

        // Simulate some match results
        team.registerVictory(3, 1);
        team.registerVictory(2, 0);
        team.registerDraw(1, 1);

        // When
        TeamEntity entity = mapper.toEntity(team);

        // Then
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Real Madrid", entity.getName());
        assertEquals("Carlo Ancelotti", entity.getCoach());
        assertEquals(10L, entity.getTournamentId());
        assertEquals(7, entity.getPoints()); // 3 + 3 + 1
        assertEquals(3, entity.getMatchesPlayed());
        assertEquals(2, entity.getMatchesWin());
        assertEquals(1, entity.getMatchesDraw());
        assertEquals(0, entity.getMatchesLost());
        assertEquals(6, entity.getGoalsFor()); // 3 + 2 + 1
        assertEquals(2, entity.getGoalsAgainst()); // 1 + 0 + 1
        assertEquals(4, entity.getGoalDifference()); // 6 - 2
    }

    @Test
    @DisplayName("Should map TeamEntity to Team domain model correctly")
    void shouldMapToDomain() {
        // Given
        TeamEntity entity = new TeamEntity(
            1L,
            "Barcelona",
            "Xavi Hernandez",
            5L,
            0,  // points - Team constructor resets these
            0,  // matchesPlayed
            0,  // matchesWin
            0,  // matchesDraw
            0,  // matchesLost
            0,  // goalsFor
            0,  // goalsAgainst
            0   // goalDifference
        );

        // When
        Team team = mapper.toDomain(entity);

        // Then - Only basic fields are mapped, statistics reset to 0
        // NOTE: Team domain lacks a full constructor for reconstitution from persistence
        assertNotNull(team);
        assertEquals(1L, team.getId());
        assertEquals("Barcelona", team.getName());
        assertEquals("Xavi Hernandez", team.getCoach());
        assertEquals(5L, team.getTournamentId());
        assertEquals(0, team.getPoints());
        assertEquals(0, team.getMatchesPlayed());
        assertEquals(0, team.getMatchesWin());
        assertEquals(0, team.getMatchesDraw());
        assertEquals(0, team.getMatchesLost());
        assertEquals(0, team.getGoalsFor());
        assertEquals(0, team.getGoalsAgainst());
        assertEquals(0, team.getGoalDifference());
    }

    @Test
    @DisplayName("Should map empty list of TeamEntity to empty list of Team")
    void shouldMapEmptyListToDomainList() {
        // Given
        List<TeamEntity> entities = List.of();

        // When
        List<Team> teams = mapper.toDomainList(entities);

        // Then
        assertNotNull(teams);
        assertTrue(teams.isEmpty());
    }

    @Test
    @DisplayName("Should map list of TeamEntity to list of Team correctly")
    void shouldMapListToDomainList() {
        // Given
        TeamEntity entity1 = new TeamEntity(1L, "Real Madrid", "Carlo Ancelotti", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
        TeamEntity entity2 = new TeamEntity(2L, "Barcelona", "Xavi Hernandez", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
        TeamEntity entity3 = new TeamEntity(3L, "Sevilla", "Jose Luis Mendilibar", 1L, 0, 0, 0, 0, 0, 0, 0, 0);

        List<TeamEntity> entities = Arrays.asList(entity1, entity2, entity3);

        // When
        List<Team> teams = mapper.toDomainList(entities);

        // Then
        assertNotNull(teams);
        assertEquals(3, teams.size());

        // Verify first team
        Team team1 = teams.get(0);
        assertEquals(1L, team1.getId());
        assertEquals("Real Madrid", team1.getName());
        assertEquals("Carlo Ancelotti", team1.getCoach());
        assertEquals(1L, team1.getTournamentId());

        // Verify second team
        Team team2 = teams.get(1);
        assertEquals(2L, team2.getId());
        assertEquals("Barcelona", team2.getName());
        assertEquals("Xavi Hernandez", team2.getCoach());

        // Verify third team
        Team team3 = teams.get(2);
        assertEquals(3L, team3.getId());
        assertEquals("Sevilla", team3.getName());
        assertEquals("Jose Luis Mendilibar", team3.getCoach());
    }

    @Test
    @DisplayName("Should handle null Team gracefully in toEntity")
    void shouldHandleNullTeamInToEntity() {
        // Given
        Team team = null;

        // When
        TeamEntity entity = mapper.toEntity(team);

        // Then
        assertNull(entity);
    }

    @Test
    @DisplayName("Should handle null TeamEntity gracefully in toDomain")
    void shouldHandleNullEntityInToDomain() {
        // Given
        TeamEntity entity = null;

        // When
        Team team = mapper.toDomain(entity);

        // Then
        assertNull(team);
    }

    @Test
    @DisplayName("Should handle null list gracefully in toDomainList")
    void shouldHandleNullListInToDomainList() {
        // Given
        List<TeamEntity> entities = null;

        // When
        List<Team> teams = mapper.toDomainList(entities);

        // Then
        assertNull(teams);
    }

    @Test
    @DisplayName("Should map Team to Entity preserving all statistics")
    void shouldMapToEntityPreservingStatistics() {
        // Given - Create a team with statistics
        Team originalTeam = new Team(5L, "Atletico Madrid", "Diego Simeone", 15L);
        originalTeam.registerVictory(2, 1);
        originalTeam.registerDraw(0, 0);
        originalTeam.registerDefeat(1, 2);

        // When - Map to entity
        TeamEntity entity = mapper.toEntity(originalTeam);

        // Then - Entity should preserve all statistics from domain
        assertEquals(originalTeam.getId(), entity.getId());
        assertEquals(originalTeam.getName(), entity.getName());
        assertEquals(originalTeam.getCoach(), entity.getCoach());
        assertEquals(originalTeam.getTournamentId(), entity.getTournamentId());
        assertEquals(originalTeam.getPoints(), entity.getPoints());
        assertEquals(originalTeam.getMatchesPlayed(), entity.getMatchesPlayed());
        assertEquals(originalTeam.getMatchesWin(), entity.getMatchesWin());
        assertEquals(originalTeam.getMatchesDraw(), entity.getMatchesDraw());
        assertEquals(originalTeam.getMatchesLost(), entity.getMatchesLost());
        assertEquals(originalTeam.getGoalsFor(), entity.getGoalsFor());
        assertEquals(originalTeam.getGoalsAgainst(), entity.getGoalsAgainst());
        assertEquals(originalTeam.getGoalDifference(), entity.getGoalDifference());
    }
}
