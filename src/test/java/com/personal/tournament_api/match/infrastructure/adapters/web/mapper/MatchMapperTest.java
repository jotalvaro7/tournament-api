package com.personal.tournament_api.match.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.match.application.usecases.CreateMatchUseCase;
import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase;
import com.personal.tournament_api.match.application.usecases.UpdateMatchUseCase;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.FinishMatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MatchMapper Unit Tests")
class MatchMapperTest {

    private MatchMapper mapper;
    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2025, 11, 15, 15, 0);

    @BeforeEach
    void setUp() {
        mapper = new MatchMapperImpl();
    }

    @Test
    @DisplayName("Should map MatchRequestDTO to CreateMatchCommand")
    void shouldMapRequestToCreateCommand() {
        // Given
        Long tournamentId = 10L;
        MatchRequestDTO request = new MatchRequestDTO(1L, 2L, TEST_DATE, "Stadium A");

        // When
        CreateMatchUseCase.CreateMatchCommand command = mapper.toCreateCommand(tournamentId, request);

        // Then
        assertNotNull(command);
        assertEquals(tournamentId, command.tournamentId());
        assertEquals(1L, command.homeTeamId());
        assertEquals(2L, command.awayTeamId());
        assertEquals(TEST_DATE, command.matchDate());
        assertEquals("Stadium A", command.field());
    }

    @Test
    @DisplayName("Should map MatchRequestDTO to UpdateMatchCommand")
    void shouldMapRequestToUpdateCommand() {
        // Given
        Long matchId = 1L;
        MatchRequestDTO request = new MatchRequestDTO(1L, 2L, TEST_DATE, "Stadium B");

        // When
        UpdateMatchUseCase.UpdateMatchCommand command = mapper.toUpdateCommand(matchId, request);

        // Then
        assertNotNull(command);
        assertEquals(matchId, command.matchId());
        assertEquals(TEST_DATE, command.matchDate());
        assertEquals("Stadium B", command.field());
    }

    @Test
    @DisplayName("Should map FinishMatchRequestDTO to FinishMatchCommand")
    void shouldMapFinishRequestToCommand() {
        // Given
        Long matchId = 1L;
        FinishMatchRequestDTO request = new FinishMatchRequestDTO(3, 1);

        // When
        FinishMatchUseCase.FinishMatchCommand command = mapper.toFinishCommand(matchId, request);

        // Then
        assertNotNull(command);
        assertEquals(matchId, command.matchId());
        assertEquals(3, command.homeTeamScore());
        assertEquals(1, command.awayTeamScore());
    }

    @Test
    @DisplayName("Should map Match to MatchResponseDTO")
    void shouldMapMatchToResponse() {
        // Given
        Match match = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");

        // When
        MatchResponseDTO response = mapper.toResponse(match);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(10L, response.tournamentId());
        assertEquals(1L, response.homeTeamId());
        assertEquals(2L, response.awayTeamId());
        assertNull(response.homeTeamScore());
        assertNull(response.awayTeamScore());
        assertEquals(TEST_DATE, response.matchDate());
        assertEquals("Stadium A", response.field());
        assertEquals(MatchStatus.SCHEDULED, response.status());
    }

    @Test
    @DisplayName("Should map Match with scores to MatchResponseDTO")
    void shouldMapMatchWithScoresToResponse() {
        // Given
        Match match = new Match(1L, 10L, 1L, 2L, 3, 1,
            TEST_DATE, "Stadium A", MatchStatus.FINISHED);

        // When
        MatchResponseDTO response = mapper.toResponse(match);

        // Then
        assertNotNull(response);
        assertEquals(3, response.homeTeamScore());
        assertEquals(1, response.awayTeamScore());
        assertEquals(MatchStatus.FINISHED, response.status());
    }

    @Test
    @DisplayName("Should map list of matches to list of responses")
    void shouldMapListOfMatchesToResponses() {
        // Given
        Match match1 = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
        Match match2 = new Match(2L, 10L, 3L, 4L, 2, 1,
            TEST_DATE.plusDays(1), "Stadium B", MatchStatus.FINISHED);
        List<Match> matches = Arrays.asList(match1, match2);

        // When
        List<MatchResponseDTO> responses = mapper.toResponseList(matches);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals(MatchStatus.SCHEDULED, responses.get(0).status());
        assertEquals(2L, responses.get(1).id());
        assertEquals(MatchStatus.FINISHED, responses.get(1).status());
    }

    @Test
    @DisplayName("Should handle empty list when mapping to responses")
    void shouldHandleEmptyListWhenMappingToResponses() {
        // Given
        List<Match> matches = List.of();

        // When
        List<MatchResponseDTO> responses = mapper.toResponseList(matches);

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should map zero scores in FinishMatchCommand")
    void shouldMapZeroScoresInFinishCommand() {
        // Given
        Long matchId = 1L;
        FinishMatchRequestDTO request = new FinishMatchRequestDTO(0, 0);

        // When
        FinishMatchUseCase.FinishMatchCommand command = mapper.toFinishCommand(matchId, request);

        // Then
        assertNotNull(command);
        assertEquals(0, command.homeTeamScore());
        assertEquals(0, command.awayTeamScore());
    }

    @Test
    @DisplayName("Should preserve match status in response mapping")
    void shouldPreserveMatchStatusInResponseMapping() {
        // Given & When & Then - SCHEDULED
        Match scheduledMatch = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
        MatchResponseDTO scheduledResponse = mapper.toResponse(scheduledMatch);
        assertEquals(MatchStatus.SCHEDULED, scheduledResponse.status());

        // Given & When & Then - FINISHED
        Match finishedMatch = new Match(2L, 10L, 1L, 2L, 3, 1,
            TEST_DATE, "Stadium A", MatchStatus.FINISHED);
        MatchResponseDTO finishedResponse = mapper.toResponse(finishedMatch);
        assertEquals(MatchStatus.FINISHED, finishedResponse.status());

        // Given & When & Then - POSTPONED
        Match postponedMatch = new Match(3L, 10L, 1L, 2L, null, null,
            TEST_DATE, "Stadium A", MatchStatus.POSTPONED);
        MatchResponseDTO postponedResponse = mapper.toResponse(postponedMatch);
        assertEquals(MatchStatus.POSTPONED, postponedResponse.status());
    }
}
