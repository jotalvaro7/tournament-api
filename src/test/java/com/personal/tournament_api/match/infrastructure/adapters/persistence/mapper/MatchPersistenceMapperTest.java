package com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MatchPersistenceMapper Unit Tests")
class MatchPersistenceMapperTest {

    private MatchPersistenceMapper mapper;
    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2025, 11, 15, 15, 0);

    @BeforeEach
    void setUp() {
        mapper = new MatchPersistenceMapperImpl();
    }

    @Test
    @DisplayName("Should map domain Match to MatchEntity")
    void shouldMapDomainMatchToEntity() {
        // Given
        Match match = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");

        // When
        MatchEntity entity = mapper.toEntity(match);

        // Then
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getTournamentId());
        assertEquals(1L, entity.getHomeTeamId());
        assertEquals(2L, entity.getAwayTeamId());
        assertNull(entity.getHomeTeamScore());
        assertNull(entity.getAwayTeamScore());
        assertEquals(TEST_DATE, entity.getMatchDate());
        assertEquals("Stadium A", entity.getField());
        assertEquals(MatchStatus.SCHEDULED, entity.getStatus());
    }

    @Test
    @DisplayName("Should map domain Match with scores to MatchEntity")
    void shouldMapDomainMatchWithScoresToEntity() {
        // Given
        Match match = new Match(1L, 10L, 1L, 2L, 3, 1,
            TEST_DATE, "Stadium A", MatchStatus.FINISHED);

        // When
        MatchEntity entity = mapper.toEntity(match);

        // Then
        assertNotNull(entity);
        assertEquals(3, entity.getHomeTeamScore());
        assertEquals(1, entity.getAwayTeamScore());
        assertEquals(MatchStatus.FINISHED, entity.getStatus());
    }

    @Test
    @DisplayName("Should return null when mapping null Match to Entity")
    void shouldReturnNullWhenMappingNullMatchToEntity() {
        // When
        MatchEntity entity = mapper.toEntity(null);

        // Then
        assertNull(entity);
    }

    @Test
    @DisplayName("Should map MatchEntity to domain Match")
    void shouldMapEntityToDomainMatch() {
        // Given
        MatchEntity entity = new MatchEntity(1L, 10L, 1L, 2L, null, null,
            TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);

        // When
        Match match = mapper.toDomain(entity);

        // Then
        assertNotNull(match);
        assertEquals(1L, match.getId());
        assertEquals(10L, match.getTournamentId());
        assertEquals(1L, match.getHomeTeamId());
        assertEquals(2L, match.getAwayTeamId());
        assertNull(match.getHomeTeamScore());
        assertNull(match.getAwayTeamScore());
        assertEquals(TEST_DATE, match.getMatchDate());
        assertEquals("Stadium A", match.getField());
        assertEquals(MatchStatus.SCHEDULED, match.getStatus());
    }

    @Test
    @DisplayName("Should map MatchEntity with scores to domain Match")
    void shouldMapEntityWithScoresToDomainMatch() {
        // Given
        MatchEntity entity = new MatchEntity(1L, 10L, 1L, 2L, 3, 1,
            TEST_DATE, "Stadium A", MatchStatus.FINISHED);

        // When
        Match match = mapper.toDomain(entity);

        // Then
        assertNotNull(match);
        assertEquals(3, match.getHomeTeamScore());
        assertEquals(1, match.getAwayTeamScore());
        assertEquals(MatchStatus.FINISHED, match.getStatus());
    }

    @Test
    @DisplayName("Should return null when mapping null Entity to Match")
    void shouldReturnNullWhenMappingNullEntityToMatch() {
        // When
        Match match = mapper.toDomain(null);

        // Then
        assertNull(match);
    }

    @Test
    @DisplayName("Should map list of entities to list of domain matches")
    void shouldMapListOfEntitiesToListOfMatches() {
        // Given
        MatchEntity entity1 = new MatchEntity(1L, 10L, 1L, 2L, null, null,
            TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
        MatchEntity entity2 = new MatchEntity(2L, 10L, 3L, 4L, 2, 1,
            TEST_DATE.plusDays(1), "Stadium B", MatchStatus.FINISHED);
        List<MatchEntity> entities = Arrays.asList(entity1, entity2);

        // When
        List<Match> matches = mapper.toDomainList(entities);

        // Then
        assertNotNull(matches);
        assertEquals(2, matches.size());
        assertEquals(1L, matches.get(0).getId());
        assertEquals(2L, matches.get(1).getId());
    }

    @Test
    @DisplayName("Should return null when mapping null list")
    void shouldReturnNullWhenMappingNullList() {
        // When
        List<Match> matches = mapper.toDomainList(null);

        // Then
        assertNull(matches);
    }

    @Test
    @DisplayName("Should map empty list correctly")
    void shouldMapEmptyListCorrectly() {
        // Given
        List<MatchEntity> entities = List.of();

        // When
        List<Match> matches = mapper.toDomainList(entities);

        // Then
        assertNotNull(matches);
        assertTrue(matches.isEmpty());
    }

    @Test
    @DisplayName("Should preserve all match statuses in mapping")
    void shouldPreserveAllMatchStatusesInMapping() {
        // Given & When & Then - SCHEDULED
        Match scheduledMatch = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
        MatchEntity scheduledEntity = mapper.toEntity(scheduledMatch);
        assertEquals(MatchStatus.SCHEDULED, scheduledEntity.getStatus());

        // Given & When & Then - FINISHED
        Match finishedMatch = new Match(2L, 10L, 1L, 2L, 3, 1,
            TEST_DATE, "Stadium A", MatchStatus.FINISHED);
        MatchEntity finishedEntity = mapper.toEntity(finishedMatch);
        assertEquals(MatchStatus.FINISHED, finishedEntity.getStatus());

        // Given & When & Then - POSTPONED
        Match postponedMatch = new Match(3L, 10L, 1L, 2L, null, null,
            TEST_DATE, "Stadium A", MatchStatus.POSTPONED);
        MatchEntity postponedEntity = mapper.toEntity(postponedMatch);
        assertEquals(MatchStatus.POSTPONED, postponedEntity.getStatus());
    }
}
