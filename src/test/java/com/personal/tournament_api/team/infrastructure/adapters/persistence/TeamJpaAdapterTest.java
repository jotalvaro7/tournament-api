package com.personal.tournament_api.team.infrastructure.adapters.persistence;

import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.entity.TeamEntity;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.mapper.TeamPersistenceMapper;
import com.personal.tournament_api.team.infrastructure.adapters.persistence.repository.TeamJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamJpaAdapter Unit Tests")
class TeamJpaAdapterTest {

    @Mock
    private TeamJpaRepository teamJpaRepository;

    @Mock
    private TeamPersistenceMapper mapper;

    @InjectMocks
    private TeamJpaAdapter teamJpaAdapter;

    private Team team;
    private TeamEntity teamEntity;

    @BeforeEach
    void setUp() {
        team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 10L);
        teamEntity = new TeamEntity(
            1L,
            "Real Madrid",
            "Carlo Ancelotti",
            10L,
            0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save team and return mapped domain object")
        void shouldSaveTeamAndReturnMappedDomainObject() {
            // Given
            when(mapper.toEntity(team)).thenReturn(teamEntity);
            when(teamJpaRepository.save(teamEntity)).thenReturn(teamEntity);
            when(mapper.toDomain(teamEntity)).thenReturn(team);

            // When
            Team result = teamJpaAdapter.save(team);

            // Then
            assertNotNull(result);
            assertEquals("Real Madrid", result.getName());
            assertEquals("Carlo Ancelotti", result.getCoach());
            assertEquals(10L, result.getTournamentId());

            verify(mapper, times(1)).toEntity(team);
            verify(teamJpaRepository, times(1)).save(teamEntity);
            verify(mapper, times(1)).toDomain(teamEntity);
        }

        @Test
        @DisplayName("Should invoke mapper and repository in correct order")
        void shouldInvokeMapperAndRepositoryInCorrectOrder() {
            // Given
            when(mapper.toEntity(any(Team.class))).thenReturn(teamEntity);
            when(teamJpaRepository.save(any(TeamEntity.class))).thenReturn(teamEntity);
            when(mapper.toDomain(any(TeamEntity.class))).thenReturn(team);

            // When
            teamJpaAdapter.save(team);

            // Then
            var inOrder = inOrder(mapper, teamJpaRepository, mapper);
            inOrder.verify(mapper).toEntity(team);
            inOrder.verify(teamJpaRepository).save(teamEntity);
            inOrder.verify(mapper).toDomain(teamEntity);
        }

        @Test
        @DisplayName("Should save team with updated statistics")
        void shouldSaveTeamWithUpdatedStatistics() {
            // Given
            Team teamWithStats = new Team(1L, "Real Madrid", "Carlo Ancelotti", 10L);
            teamWithStats.registerVictory(3, 1);

            TeamEntity entityWithStats = new TeamEntity(
                1L, "Real Madrid", "Carlo Ancelotti", 10L,
                3, 1, 1, 0, 0, 3, 1, 2
            );

            when(mapper.toEntity(teamWithStats)).thenReturn(entityWithStats);
            when(teamJpaRepository.save(entityWithStats)).thenReturn(entityWithStats);
            when(mapper.toDomain(entityWithStats)).thenReturn(teamWithStats);

            // When
            Team result = teamJpaAdapter.save(teamWithStats);

            // Then
            assertNotNull(result);
            assertEquals(3, result.getPoints());
            assertEquals(1, result.getMatchesWin());

            verify(mapper, times(1)).toEntity(teamWithStats);
            verify(teamJpaRepository, times(1)).save(entityWithStats);
        }
    }

    @Nested
    @DisplayName("Find By Id Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should find team by id when exists")
        void shouldFindTeamByIdWhenExists() {
            // Given
            when(teamJpaRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
            when(mapper.toDomain(teamEntity)).thenReturn(team);

            // When
            Optional<Team> result = teamJpaAdapter.findById(1L);

            // Then
            assertTrue(result.isPresent());
            assertEquals("Real Madrid", result.get().getName());
            assertEquals(1L, result.get().getId());

            verify(teamJpaRepository, times(1)).findById(1L);
            verify(mapper, times(1)).toDomain(teamEntity);
        }

        @Test
        @DisplayName("Should return empty optional when team not found")
        void shouldReturnEmptyOptionalWhenTeamNotFound() {
            // Given
            when(teamJpaRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<Team> result = teamJpaAdapter.findById(999L);

            // Then
            assertFalse(result.isPresent());

            verify(teamJpaRepository, times(1)).findById(999L);
            verify(mapper, never()).toDomain(any(TeamEntity.class));
        }

        @Test
        @DisplayName("Should map entity to domain when team is found")
        void shouldMapEntityToDomainWhenTeamIsFound() {
            // Given
            when(teamJpaRepository.findById(1L)).thenReturn(Optional.of(teamEntity));
            when(mapper.toDomain(teamEntity)).thenReturn(team);

            // When
            Optional<Team> result = teamJpaAdapter.findById(1L);

            // Then
            assertTrue(result.isPresent());
            verify(mapper, times(1)).toDomain(teamEntity);
        }
    }

    @Nested
    @DisplayName("Find All By Order By Name Asc Tests")
    class FindAllByOrderByNameAscTests {

        @Test
        @DisplayName("Should find all teams ordered by name ascending")
        void shouldFindAllTeamsOrderedByNameAsc() {
            // Given
            TeamEntity entity1 = new TeamEntity(1L, "Barcelona", "Xavi Hernandez", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
            TeamEntity entity2 = new TeamEntity(2L, "Real Madrid", "Carlo Ancelotti", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
            TeamEntity entity3 = new TeamEntity(3L, "Sevilla", "Jose Luis Mendilibar", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
            List<TeamEntity> entities = Arrays.asList(entity1, entity2, entity3);

            Team team1 = new Team(1L, "Barcelona", "Xavi Hernandez", 1L);
            Team team2 = new Team(2L, "Real Madrid", "Carlo Ancelotti", 1L);
            Team team3 = new Team(3L, "Sevilla", "Jose Luis Mendilibar", 1L);
            List<Team> teams = Arrays.asList(team1, team2, team3);

            when(teamJpaRepository.findAllByOrderByNameAsc()).thenReturn(entities);
            when(mapper.toDomainList(entities)).thenReturn(teams);

            // When
            List<Team> result = teamJpaAdapter.findAllByOrderByNameAsc();

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("Barcelona", result.get(0).getName());
            assertEquals("Real Madrid", result.get(1).getName());
            assertEquals("Sevilla", result.get(2).getName());

            verify(teamJpaRepository, times(1)).findAllByOrderByNameAsc();
            verify(mapper, times(1)).toDomainList(entities);
        }

        @Test
        @DisplayName("Should return empty list when no teams exist")
        void shouldReturnEmptyListWhenNoTeamsExist() {
            // Given
            List<TeamEntity> emptyEntities = List.of();
            List<Team> emptyTeams = List.of();

            when(teamJpaRepository.findAllByOrderByNameAsc()).thenReturn(emptyEntities);
            when(mapper.toDomainList(emptyEntities)).thenReturn(emptyTeams);

            // When
            List<Team> result = teamJpaAdapter.findAllByOrderByNameAsc();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(teamJpaRepository, times(1)).findAllByOrderByNameAsc();
            verify(mapper, times(1)).toDomainList(emptyEntities);
        }

        @Test
        @DisplayName("Should delegate to repository and mapper")
        void shouldDelegateToRepositoryAndMapper() {
            // Given
            List<TeamEntity> entities = List.of(teamEntity);
            List<Team> teams = List.of(team);

            when(teamJpaRepository.findAllByOrderByNameAsc()).thenReturn(entities);
            when(mapper.toDomainList(entities)).thenReturn(teams);

            // When
            teamJpaAdapter.findAllByOrderByNameAsc();

            // Then
            verify(teamJpaRepository, times(1)).findAllByOrderByNameAsc();
            verify(mapper, times(1)).toDomainList(entities);
        }
    }

    @Nested
    @DisplayName("Delete By Id Tests")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete team by id")
        void shouldDeleteTeamById() {
            // Given
            doNothing().when(teamJpaRepository).deleteById(1L);

            // When
            teamJpaAdapter.deleteById(1L);

            // Then
            verify(teamJpaRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should delegate deletion to repository")
        void shouldDelegateDeletionToRepository() {
            // Given
            Long teamId = 999L;
            doNothing().when(teamJpaRepository).deleteById(teamId);

            // When
            teamJpaAdapter.deleteById(teamId);

            // Then
            verify(teamJpaRepository, times(1)).deleteById(teamId);
        }

        @Test
        @DisplayName("Should not invoke mapper when deleting")
        void shouldNotInvokeMapperWhenDeleting() {
            // Given
            doNothing().when(teamJpaRepository).deleteById(1L);

            // When
            teamJpaAdapter.deleteById(1L);

            // Then
            verify(teamJpaRepository, times(1)).deleteById(1L);
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("Exists By Name Tests")
    class ExistsByNameTests {

        @Test
        @DisplayName("Should return true when team name exists")
        void shouldReturnTrueWhenTeamNameExists() {
            // Given
            when(teamJpaRepository.existsByName("Real Madrid")).thenReturn(true);

            // When
            boolean result = teamJpaAdapter.existsByName("Real Madrid");

            // Then
            assertTrue(result);
            verify(teamJpaRepository, times(1)).existsByName("Real Madrid");
        }

        @Test
        @DisplayName("Should return false when team name does not exist")
        void shouldReturnFalseWhenTeamNameDoesNotExist() {
            // Given
            when(teamJpaRepository.existsByName("Nonexistent Team")).thenReturn(false);

            // When
            boolean result = teamJpaAdapter.existsByName("Nonexistent Team");

            // Then
            assertFalse(result);
            verify(teamJpaRepository, times(1)).existsByName("Nonexistent Team");
        }

        @Test
        @DisplayName("Should delegate to repository for existence check")
        void shouldDelegateToRepositoryForExistenceCheck() {
            // Given
            String teamName = "Barcelona";
            when(teamJpaRepository.existsByName(teamName)).thenReturn(true);

            // When
            teamJpaAdapter.existsByName(teamName);

            // Then
            verify(teamJpaRepository, times(1)).existsByName(teamName);
            verifyNoInteractions(mapper);
        }
    }

    @Nested
    @DisplayName("Exists By Name And Id Not Tests")
    class ExistsByNameAndIdNotTests {

        @Test
        @DisplayName("Should return true when name exists for different team")
        void shouldReturnTrueWhenNameExistsForDifferentTeam() {
            // Given
            when(teamJpaRepository.existsByNameAndIdNot("Barcelona", 1L)).thenReturn(true);

            // When
            boolean result = teamJpaAdapter.existsByNameAndIdNot("Barcelona", 1L);

            // Then
            assertTrue(result);
            verify(teamJpaRepository, times(1)).existsByNameAndIdNot("Barcelona", 1L);
        }

        @Test
        @DisplayName("Should return false when name does not exist for other teams")
        void shouldReturnFalseWhenNameDoesNotExistForOtherTeams() {
            // Given
            when(teamJpaRepository.existsByNameAndIdNot("Real Madrid CF", 1L)).thenReturn(false);

            // When
            boolean result = teamJpaAdapter.existsByNameAndIdNot("Real Madrid CF", 1L);

            // Then
            assertFalse(result);
            verify(teamJpaRepository, times(1)).existsByNameAndIdNot("Real Madrid CF", 1L);
        }

        @Test
        @DisplayName("Should delegate to repository for uniqueness check excluding id")
        void shouldDelegateToRepositoryForUniquenessCheckExcludingId() {
            // Given
            String teamName = "Sevilla";
            Long teamId = 5L;
            when(teamJpaRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(false);

            // When
            teamJpaAdapter.existsByNameAndIdNot(teamName, teamId);

            // Then
            verify(teamJpaRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
            verifyNoInteractions(mapper);
        }

        @Test
        @DisplayName("Should allow same name for same team id")
        void shouldAllowSameNameForSameTeamId() {
            // Given - Team with id=1 is named "Real Madrid"
            // Checking if "Real Madrid" exists for teams other than id=1
            when(teamJpaRepository.existsByNameAndIdNot("Real Madrid", 1L)).thenReturn(false);

            // When
            boolean result = teamJpaAdapter.existsByNameAndIdNot("Real Madrid", 1L);

            // Then
            assertFalse(result); // Should be false because it's the same team
            verify(teamJpaRepository, times(1)).existsByNameAndIdNot("Real Madrid", 1L);
        }
    }
}
