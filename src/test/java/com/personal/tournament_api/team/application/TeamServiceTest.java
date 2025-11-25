package com.personal.tournament_api.team.application;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase.CreateTeamCommand;
import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase.UpdateTeamCommand;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.exceptions.DuplicateTeamNameException;
import com.personal.tournament_api.team.domain.exceptions.TeamHasMatchesException;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamService Unit Tests")
class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamDomainService teamDomainService;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private CreateTeamCommand createCommand;
    private UpdateTeamCommand updateCommand;

    @BeforeEach
    void setUp() {
        team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 10L);
        createCommand = new CreateTeamCommand("Real Madrid", "Carlo Ancelotti", 10L);
        updateCommand = new UpdateTeamCommand(1L, "Real Madrid CF", "Carlo Ancelotti");
    }

    @Nested
    @DisplayName("Create Team Tests")
    class CreateTeamTests {

        @Test
        @DisplayName("Should create team successfully")
        void shouldCreateTeamSuccessfully() {
            // Given
            doNothing().when(teamDomainService).validateUniqueTeamName(eq("Real Madrid"), eq(teamRepository));
            when(teamRepository.save(any(Team.class))).thenReturn(team);

            // When
            Team result = teamService.create(createCommand);

            // Then
            assertNotNull(result);
            assertEquals("Real Madrid", result.getName());
            assertEquals("Carlo Ancelotti", result.getCoach());
            assertEquals(10L, result.getTournamentId());

            verify(teamDomainService, times(1)).validateUniqueTeamName("Real Madrid", teamRepository);
            verify(teamRepository, times(1)).save(any(Team.class));
        }

        @Test
        @DisplayName("Should throw DuplicateTeamNameException when name already exists")
        void shouldThrowExceptionWhenNameExists() {
            // Given
            doThrow(new DuplicateTeamNameException("Real Madrid"))
                .when(teamDomainService).validateUniqueTeamName(eq("Real Madrid"), eq(teamRepository));

            // When & Then
            assertThrows(DuplicateTeamNameException.class, () -> {
                teamService.create(createCommand);
            });

            verify(teamDomainService, times(1)).validateUniqueTeamName("Real Madrid", teamRepository);
            verify(teamRepository, never()).save(any(Team.class));
        }
    }

    @Nested
    @DisplayName("Update Team Tests")
    class UpdateTeamTests {

        @Test
        @DisplayName("Should update team successfully")
        void shouldUpdateTeamSuccessfully() {
            // Given
            doNothing().when(teamDomainService).validateUniqueNameForUpdate(
                eq("Real Madrid CF"), eq(1L), eq(teamRepository));
            when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
            when(teamRepository.save(any(Team.class))).thenReturn(team);

            // When
            Team result = teamService.update(updateCommand);

            // Then
            assertNotNull(result);
            assertEquals("Real Madrid CF", result.getName());
            assertEquals("Carlo Ancelotti", result.getCoach());

            verify(teamDomainService, times(1)).validateUniqueNameForUpdate("Real Madrid CF", 1L, teamRepository);
            verify(teamRepository, times(1)).findById(1L);
            verify(teamRepository, times(1)).save(team);
        }

        @Test
        @DisplayName("Should throw TeamNotFoundException when team not found")
        void shouldThrowExceptionWhenTeamNotFound() {
            // Given
            doNothing().when(teamDomainService).validateUniqueNameForUpdate(
                eq("Real Madrid CF"), eq(999L), eq(teamRepository));
            when(teamRepository.findById(999L)).thenReturn(Optional.empty());

            UpdateTeamCommand command = new UpdateTeamCommand(999L, "Real Madrid CF", "Carlo Ancelotti");

            // When & Then
            assertThrows(TeamNotFoundException.class, () -> {
                teamService.update(command);
            });

            verify(teamRepository, times(1)).findById(999L);
            verify(teamRepository, never()).save(any(Team.class));
        }

        @Test
        @DisplayName("Should throw DuplicateTeamNameException when updating to existing name")
        void shouldThrowExceptionWhenUpdatingToExistingName() {
            // Given
            doThrow(new DuplicateTeamNameException("Barcelona"))
                .when(teamDomainService).validateUniqueNameForUpdate(
                    eq("Barcelona"), eq(1L), eq(teamRepository));

            UpdateTeamCommand command = new UpdateTeamCommand(1L, "Barcelona", "Xavi Hernandez");

            // When & Then
            assertThrows(DuplicateTeamNameException.class, () -> {
                teamService.update(command);
            });

            verify(teamDomainService, times(1)).validateUniqueNameForUpdate("Barcelona", 1L, teamRepository);
            verify(teamRepository, never()).findById(any());
            verify(teamRepository, never()).save(any(Team.class));
        }
    }

    @Nested
    @DisplayName("Get Team Tests")
    class GetTeamTests {

        @Test
        @DisplayName("Should get team by id when exists")
        void shouldGetTeamByIdWhenExists() {
            // Given
            when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

            // When
            Optional<Team> result = teamService.getById(1L);

            // Then
            assertTrue(result.isPresent());
            assertEquals("Real Madrid", result.get().getName());
            assertEquals(1L, result.get().getId());

            verify(teamRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when team not found")
        void shouldReturnEmptyWhenTeamNotFound() {
            // Given
            when(teamRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<Team> result = teamService.getById(999L);

            // Then
            assertFalse(result.isPresent());

            verify(teamRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Should get all teams ordered by name ascending")
        void shouldGetAllTeamsOrderedByNameAsc() {
            // Given
            Team team1 = new Team(1L, "Barcelona", "Xavi Hernandez", 1L);
            Team team2 = new Team(2L, "Real Madrid", "Carlo Ancelotti", 1L);
            Team team3 = new Team(3L, "Sevilla", "Jose Luis Mendilibar", 1L);
            List<Team> teams = Arrays.asList(team1, team2, team3);

            when(teamRepository.findAllByTournamentIdOrderByNameAsc(1L)).thenReturn(teams);

            // When
            List<Team> result = teamService.getAllByTournamentIdOrderByNameAsc(1L);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals("Barcelona", result.get(0).getName());
            assertEquals("Real Madrid", result.get(1).getName());
            assertEquals("Sevilla", result.get(2).getName());

            verify(teamRepository, times(1)).findAllByTournamentIdOrderByNameAsc(1L);
        }

        @Test
        @DisplayName("Should return empty list when no teams exist")
        void shouldReturnEmptyListWhenNoTeamsExist() {
            // Given
            when(teamRepository.findAllByTournamentIdOrderByNameAsc(1L)).thenReturn(List.of());

            // When
            List<Team> result = teamService.getAllByTournamentIdOrderByNameAsc(1L);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(teamRepository, times(1)).findAllByTournamentIdOrderByNameAsc(1L);
        }
    }

    @Nested
    @DisplayName("Delete Team Tests")
    class DeleteTeamTests {

        @Test
        @DisplayName("Should delete team successfully when has no associated matches")
        void shouldDeleteTeamSuccessfully() {
            // Given
            when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
            when(matchRepository.findAllByTeamId(1L)).thenReturn(List.of());
            doNothing().when(teamRepository).deleteById(1L);

            // When
            teamService.delete(1L);

            // Then
            verify(teamRepository, times(1)).findById(1L);
            verify(matchRepository, times(1)).findAllByTeamId(1L);
            verify(teamRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw TeamNotFoundException when team not found")
        void shouldThrowExceptionWhenDeletingNonExistentTeam() {
            // Given
            when(teamRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TeamNotFoundException.class, () -> {
                teamService.delete(999L);
            });

            verify(teamRepository, times(1)).findById(999L);
            verify(matchRepository, never()).findAllByTeamId(any());
            verify(teamRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Should throw TeamHasMatchesException when team has associated matches")
        void shouldThrowExceptionWhenTeamHasMatches() {
            // Given
            Match match1 = new Match(1L, 1L, 1L, 2L,
                java.time.LocalDateTime.now(), "Field A");
            Match match2 = new Match(2L, 1L, 3L, 1L,
                java.time.LocalDateTime.now(), "Field B");
            List<Match> matches = Arrays.asList(match1, match2);

            when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
            when(matchRepository.findAllByTeamId(1L)).thenReturn(matches);

            // When & Then
            TeamHasMatchesException exception = assertThrows(TeamHasMatchesException.class, () -> {
                teamService.delete(1L);
            });

            assertTrue(exception.getMessage().contains("cannot be deleted"));
            assertTrue(exception.getMessage().contains("2 associated match(es)"));

            verify(teamRepository, times(1)).findById(1L);
            verify(matchRepository, times(1)).findAllByTeamId(1L);
            verify(teamRepository, never()).deleteById(any());
        }
    }
}
