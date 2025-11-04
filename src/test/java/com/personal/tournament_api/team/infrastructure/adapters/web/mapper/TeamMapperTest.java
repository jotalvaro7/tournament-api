package com.personal.tournament_api.team.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase.CreateTeamCommand;
import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase.UpdateTeamCommand;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamRequestDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TeamMapper Unit Tests")
class TeamMapperTest {

    private TeamMapper teamMapper;

    @BeforeEach
    void setUp() {
        teamMapper = Mappers.getMapper(TeamMapper.class);
    }

    @Test
    @DisplayName("Should map TeamRequestDTO to CreateTeamCommand correctly")
    void shouldMapToCreateCommand() {
        // Given
        Long tournamentId = 1L;
        TeamRequestDTO request = new TeamRequestDTO("Real Madrid", "Carlo Ancelotti");

        // When
        CreateTeamCommand command = teamMapper.toCreateCommand(tournamentId, request);

        // Then
        assertNotNull(command);
        assertEquals("Real Madrid", command.name());
        assertEquals("Carlo Ancelotti", command.coach());
        assertEquals(1L, command.tournamentId());
    }

    @Test
    @DisplayName("Should map TeamRequestDTO to UpdateTeamCommand correctly")
    void shouldMapToUpdateCommand() {
        // Given
        Long teamId = 5L;
        TeamRequestDTO request = new TeamRequestDTO("Barcelona", "Xavi Hernandez");

        // When
        UpdateTeamCommand command = teamMapper.toUpdateCommand(teamId, request);

        // Then
        assertNotNull(command);
        assertEquals(5L, command.id());
        assertEquals("Barcelona", command.name());
        assertEquals("Xavi Hernandez", command.coach());
    }

    @Test
    @DisplayName("Should map Team domain model to TeamResponseDTO correctly")
    void shouldMapToResponse() {
        // Given
        Team team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 10L);

        // Simulate some match results to test all fields
        team.registerVictory(3, 1);
        team.registerVictory(2, 0);
        team.registerDraw(1, 1);

        // When
        TeamResponseDTO response = teamMapper.toResponse(team);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Real Madrid", response.name());
        assertEquals("Carlo Ancelotti", response.coach());
        assertEquals(10L, response.tournamentId());
        assertEquals(7, response.points()); // 3 + 3 + 1
        assertEquals(3, response.matchesPlayed());
        assertEquals(2, response.matchesWin());
        assertEquals(1, response.matchesDraw());
        assertEquals(0, response.matchesLost());
        assertEquals(6, response.goalsFor()); // 3 + 2 + 1
        assertEquals(2, response.goalsAgainst()); // 1 + 0 + 1
        assertEquals(4, response.goalDifference()); // 6 - 2
    }

    @Test
    @DisplayName("Should map empty list of Teams to empty list of TeamResponseDTO")
    void shouldMapEmptyListToResponseList() {
        // Given
        List<Team> teams = List.of();

        // When
        List<TeamResponseDTO> responses = teamMapper.toResponseList(teams);

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should map list of Teams to list of TeamResponseDTO correctly")
    void shouldMapListToResponseList() {
        // Given
        Team team1 = new Team(1L, "Real Madrid", "Carlo Ancelotti", 1L);
        Team team2 = new Team(2L, "Barcelona", "Xavi Hernandez", 1L);
        Team team3 = new Team(3L, "Sevilla", "Jose Luis Mendilibar", 1L);

        List<Team> teams = Arrays.asList(team1, team2, team3);

        // When
        List<TeamResponseDTO> responses = teamMapper.toResponseList(teams);

        // Then
        assertNotNull(responses);
        assertEquals(3, responses.size());

        // Verify first team
        TeamResponseDTO response1 = responses.get(0);
        assertEquals(1L, response1.id());
        assertEquals("Real Madrid", response1.name());
        assertEquals("Carlo Ancelotti", response1.coach());
        assertEquals(1L, response1.tournamentId());

        // Verify second team
        TeamResponseDTO response2 = responses.get(1);
        assertEquals(2L, response2.id());
        assertEquals("Barcelona", response2.name());
        assertEquals("Xavi Hernandez", response2.coach());

        // Verify third team
        TeamResponseDTO response3 = responses.get(2);
        assertEquals(3L, response3.id());
        assertEquals("Sevilla", response3.name());
        assertEquals("Jose Luis Mendilibar", response3.coach());
    }



    @Test
    @DisplayName("Should handle null Team gracefully in toResponse")
    void shouldHandleNullTeamInToResponse() {
        // Given
        Team team = null;

        // When
        TeamResponseDTO response = teamMapper.toResponse(team);

        // Then
        assertNull(response);
    }

    @Test
    @DisplayName("Should handle null list gracefully in toResponseList")
    void shouldHandleNullListInToResponseList() {
        // Given
        List<Team> teams = null;

        // When
        List<TeamResponseDTO> responses = teamMapper.toResponseList(teams);

        // Then
        assertNull(responses);
    }
}
