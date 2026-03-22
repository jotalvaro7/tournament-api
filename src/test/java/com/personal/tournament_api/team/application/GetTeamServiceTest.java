package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GetTeamService Tests")
@ExtendWith(MockitoExtension.class)
class GetTeamServiceTest {

    @Mock private TeamRepository teamRepository;

    private GetTeamService service;

    @BeforeEach
    void setUp() {
        service = new GetTeamService(teamRepository);
    }

    @Test
    @DisplayName("Should get team by id when exists")
    void shouldGetTeamByIdWhenExists() {
        // Given
        Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // When
        Optional<Team> result = service.getById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Real Madrid", result.get().getName());
    }

    @Test
    @DisplayName("Should return empty when team not found")
    void shouldReturnEmptyWhenNotFound() {
        // Given
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Team> result = service.getById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should get all teams ordered by name ascending")
    void shouldGetAllTeamsOrderedByNameAsc() {
        // Given
        List<Team> teams = List.of(
                Team.reconstitute(1L, "Barcelona", "Xavi Hernandez", 1L, 0, 0, 0, 0, 0, 0, 0, 0),
                Team.reconstitute(2L, "Real Madrid", "Carlo Ancelotti", 1L, 0, 0, 0, 0, 0, 0, 0, 0)
        );
        when(teamRepository.findAllByTournamentIdOrderByNameAsc(1L)).thenReturn(teams);

        // When
        List<Team> result = service.getAllByTournamentIdOrderByNameAsc(1L);

        // Then
        assertEquals(2, result.size());
        assertEquals("Barcelona", result.get(0).getName());
    }

    @Test
    @DisplayName("Should return empty list when no teams exist")
    void shouldReturnEmptyList() {
        // Given
        when(teamRepository.findAllByTournamentIdOrderByNameAsc(1L)).thenReturn(List.of());

        // When
        List<Team> result = service.getAllByTournamentIdOrderByNameAsc(1L);

        // Then
        assertTrue(result.isEmpty());
    }
}
