package com.personal.tournament_api.match.infrastructure.adapters.persistence;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.entity.MatchEntity;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.mapper.MatchPersistenceMapper;
import com.personal.tournament_api.match.infrastructure.adapters.persistence.repository.MatchJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MatchJpaAdapter Unit Tests")
class MatchJpaAdapterTest {

    @Mock
    private MatchJpaRepository matchJpaRepository;

    @Mock
    private MatchPersistenceMapper mapper;

    @InjectMocks
    private MatchJpaAdapter matchJpaAdapter;

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2025, 11, 15, 15, 0);
    private static final Long MATCH_ID = 1L;
    private static final Long TOURNAMENT_ID = 10L;

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save match successfully")
        void shouldSaveMatchSuccessfully() {
            // Given
            Match match = new Match(null, TOURNAMENT_ID, 1L, 2L, TEST_DATE, "Stadium A");
            MatchEntity entity = new MatchEntity(null, TOURNAMENT_ID, 1L, 2L,
                null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            MatchEntity savedEntity = new MatchEntity(MATCH_ID, TOURNAMENT_ID, 1L, 2L,
                null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            Match savedMatch = new Match(MATCH_ID, TOURNAMENT_ID, 1L, 2L, TEST_DATE, "Stadium A");

            when(mapper.toEntity(match)).thenReturn(entity);
            when(matchJpaRepository.save(entity)).thenReturn(savedEntity);
            when(mapper.toDomain(savedEntity)).thenReturn(savedMatch);

            // When
            Match result = matchJpaAdapter.save(match);

            // Then
            assertNotNull(result);
            assertEquals(MATCH_ID, result.getId());
            verify(mapper, times(1)).toEntity(match);
            verify(matchJpaRepository, times(1)).save(entity);
            verify(mapper, times(1)).toDomain(savedEntity);
        }

        @Test
        @DisplayName("Should update existing match successfully")
        void shouldUpdateExistingMatchSuccessfully() {
            // Given
            Match existingMatch = new Match(MATCH_ID, TOURNAMENT_ID, 1L, 2L,
                3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            MatchEntity entity = new MatchEntity(MATCH_ID, TOURNAMENT_ID, 1L, 2L,
                3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            MatchEntity savedEntity = new MatchEntity(MATCH_ID, TOURNAMENT_ID, 1L, 2L,
                3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);

            when(mapper.toEntity(existingMatch)).thenReturn(entity);
            when(matchJpaRepository.save(entity)).thenReturn(savedEntity);
            when(mapper.toDomain(savedEntity)).thenReturn(existingMatch);

            // When
            Match result = matchJpaAdapter.save(existingMatch);

            // Then
            assertNotNull(result);
            assertEquals(3, result.getHomeTeamScore());
            assertEquals(1, result.getAwayTeamScore());
            verify(matchJpaRepository, times(1)).save(entity);
        }

        @Test
        @DisplayName("Should call mapper and repository in correct order")
        void shouldCallMapperAndRepositoryInCorrectOrder() {
            // Given
            Match match = new Match(null, TOURNAMENT_ID, 1L, 2L, TEST_DATE, "Stadium A");
            MatchEntity entity = new MatchEntity();
            MatchEntity savedEntity = new MatchEntity();
            Match savedMatch = new Match(MATCH_ID, TOURNAMENT_ID, 1L, 2L, TEST_DATE, "Stadium A");

            when(mapper.toEntity(match)).thenReturn(entity);
            when(matchJpaRepository.save(entity)).thenReturn(savedEntity);
            when(mapper.toDomain(savedEntity)).thenReturn(savedMatch);

            // When
            matchJpaAdapter.save(match);

            // Then
            var inOrder = inOrder(mapper, matchJpaRepository);
            inOrder.verify(mapper).toEntity(match);
            inOrder.verify(matchJpaRepository).save(entity);
            inOrder.verify(mapper).toDomain(savedEntity);
        }
    }

    @Nested
    @DisplayName("Find By Id Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find match by id successfully")
        void shouldFindMatchByIdSuccessfully() {
            // Given
            MatchEntity entity = new MatchEntity(MATCH_ID, TOURNAMENT_ID, 1L, 2L,
                null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, 1L, 2L, TEST_DATE, "Stadium A");

            when(matchJpaRepository.findById(MATCH_ID)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(match);

            // When
            Optional<Match> result = matchJpaAdapter.findById(MATCH_ID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(MATCH_ID, result.get().getId());
            verify(matchJpaRepository, times(1)).findById(MATCH_ID);
            verify(mapper, times(1)).toDomain(entity);
        }

        @Test
        @DisplayName("Should return empty when match not found")
        void shouldReturnEmptyWhenMatchNotFound() {
            // Given
            when(matchJpaRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<Match> result = matchJpaAdapter.findById(999L);

            // Then
            assertFalse(result.isPresent());
            verify(matchJpaRepository, times(1)).findById(999L);
            verify(mapper, never()).toDomain(any());
        }

        @Test
        @DisplayName("Should find match with scores by id")
        void shouldFindMatchWithScoresById() {
            // Given
            MatchEntity entity = new MatchEntity(MATCH_ID, TOURNAMENT_ID, 1L, 2L,
                3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, 1L, 2L, 3, 1,
                TEST_DATE, "Stadium A", MatchStatus.FINISHED);

            when(matchJpaRepository.findById(MATCH_ID)).thenReturn(Optional.of(entity));
            when(mapper.toDomain(entity)).thenReturn(match);

            // When
            Optional<Match> result = matchJpaAdapter.findById(MATCH_ID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(3, result.get().getHomeTeamScore());
            assertEquals(1, result.get().getAwayTeamScore());
        }
    }

    @Nested
    @DisplayName("Find All By Tournament Id Tests")
    class FindAllByTournamentIdTests {

        @Test
        @DisplayName("Should find all matches by tournament id")
        void shouldFindAllMatchesByTournamentId() {
            // Given
            MatchEntity entity1 = new MatchEntity(1L, TOURNAMENT_ID, 1L, 2L,
                null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            MatchEntity entity2 = new MatchEntity(2L, TOURNAMENT_ID, 3L, 4L,
                2, 1, TEST_DATE.plusDays(1), "Stadium B", MatchStatus.FINISHED);
            List<MatchEntity> entities = Arrays.asList(entity1, entity2);

            Match match1 = new Match(1L, TOURNAMENT_ID, 1L, 2L, TEST_DATE, "Stadium A");
            Match match2 = new Match(2L, TOURNAMENT_ID, 3L, 4L, 2, 1,
                TEST_DATE.plusDays(1), "Stadium B", MatchStatus.FINISHED);
            List<Match> matches = Arrays.asList(match1, match2);

            when(matchJpaRepository.findAllByTournamentId(TOURNAMENT_ID)).thenReturn(entities);
            when(mapper.toDomainList(entities)).thenReturn(matches);

            // When
            List<Match> result = matchJpaAdapter.findAllByTournamentId(TOURNAMENT_ID);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(m -> m.getTournamentId().equals(TOURNAMENT_ID)));
            verify(matchJpaRepository, times(1)).findAllByTournamentId(TOURNAMENT_ID);
            verify(mapper, times(1)).toDomainList(entities);
        }

        @Test
        @DisplayName("Should return empty list when no matches for tournament")
        void shouldReturnEmptyListWhenNoMatchesForTournament() {
            // Given
            when(matchJpaRepository.findAllByTournamentId(TOURNAMENT_ID)).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            // When
            List<Match> result = matchJpaAdapter.findAllByTournamentId(TOURNAMENT_ID);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(matchJpaRepository, times(1)).findAllByTournamentId(TOURNAMENT_ID);
        }
    }

    @Nested
    @DisplayName("Find All By Team Id Tests")
    class FindAllByTeamIdTests {

        @Test
        @DisplayName("Should find all matches by team id")
        void shouldFindAllMatchesByTeamId() {
            // Given
            Long teamId = 1L;
            MatchEntity entity1 = new MatchEntity(1L, TOURNAMENT_ID, teamId, 2L,
                null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            MatchEntity entity2 = new MatchEntity(2L, TOURNAMENT_ID, 3L, teamId,
                null, null, TEST_DATE.plusDays(1), "Stadium B", MatchStatus.SCHEDULED);
            List<MatchEntity> entities = Arrays.asList(entity1, entity2);

            Match match1 = new Match(1L, TOURNAMENT_ID, teamId, 2L, TEST_DATE, "Stadium A");
            Match match2 = new Match(2L, TOURNAMENT_ID, 3L, teamId,
                TEST_DATE.plusDays(1), "Stadium B");
            List<Match> matches = Arrays.asList(match1, match2);

            when(matchJpaRepository.findAllByTeamId(teamId)).thenReturn(entities);
            when(mapper.toDomainList(entities)).thenReturn(matches);

            // When
            List<Match> result = matchJpaAdapter.findAllByTeamId(teamId);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(matchJpaRepository, times(1)).findAllByTeamId(teamId);
            verify(mapper, times(1)).toDomainList(entities);
        }

        @Test
        @DisplayName("Should return empty list when no matches for team")
        void shouldReturnEmptyListWhenNoMatchesForTeam() {
            // Given
            Long teamId = 1L;
            when(matchJpaRepository.findAllByTeamId(teamId)).thenReturn(List.of());
            when(mapper.toDomainList(List.of())).thenReturn(List.of());

            // When
            List<Match> result = matchJpaAdapter.findAllByTeamId(teamId);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(matchJpaRepository, times(1)).findAllByTeamId(teamId);
        }
    }

    @Nested
    @DisplayName("Delete By Id Tests")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete match by id successfully")
        void shouldDeleteMatchByIdSuccessfully() {
            // Given
            doNothing().when(matchJpaRepository).deleteById(MATCH_ID);

            // When
            matchJpaAdapter.deleteById(MATCH_ID);

            // Then
            verify(matchJpaRepository, times(1)).deleteById(MATCH_ID);
        }

        @Test
        @DisplayName("Should call repository delete method once")
        void shouldCallRepositoryDeleteMethodOnce() {
            // Given
            doNothing().when(matchJpaRepository).deleteById(MATCH_ID);

            // When
            matchJpaAdapter.deleteById(MATCH_ID);

            // Then
            verify(matchJpaRepository, times(1)).deleteById(MATCH_ID);
            verifyNoMoreInteractions(matchJpaRepository);
        }
    }

    @Nested
    @DisplayName("Delete By Tournament Id Tests")
    class DeleteByTournamentIdTests {

        @Test
        @DisplayName("Should delete all matches by tournament id successfully")
        void shouldDeleteAllMatchesByTournamentIdSuccessfully() {
            // Given
            doNothing().when(matchJpaRepository).deleteByTournamentId(TOURNAMENT_ID);

            // When
            matchJpaAdapter.deleteByTournamentId(TOURNAMENT_ID);

            // Then
            verify(matchJpaRepository, times(1)).deleteByTournamentId(TOURNAMENT_ID);
        }

        @Test
        @DisplayName("Should call repository delete by tournament method once")
        void shouldCallRepositoryDeleteByTournamentMethodOnce() {
            // Given
            doNothing().when(matchJpaRepository).deleteByTournamentId(TOURNAMENT_ID);

            // When
            matchJpaAdapter.deleteByTournamentId(TOURNAMENT_ID);

            // Then
            verify(matchJpaRepository, times(1)).deleteByTournamentId(TOURNAMENT_ID);
            verifyNoMoreInteractions(matchJpaRepository);
        }
    }
}
