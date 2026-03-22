package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GetMatchService Tests")
@ExtendWith(MockitoExtension.class)
class GetMatchServiceTest {

    @Mock private MatchRepository matchRepository;

    private GetMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new GetMatchService(matchRepository);
    }

    @Test
    @DisplayName("Should get match by id when exists")
    void shouldGetMatchByIdWhenExists() {
        // Given
        Match match = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED);
        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));

        // When
        Optional<Match> result = service.getById(MATCH_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(MATCH_ID, result.get().getId());
        assertEquals(MatchStatus.SCHEDULED, result.get().getStatus());
    }

    @Test
    @DisplayName("Should return empty when match not found")
    void shouldReturnEmptyWhenMatchNotFound() {
        // Given
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Match> result = service.getById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should get all matches by tournament id")
    void shouldGetAllMatchesByTournamentId() {
        // Given
        List<Match> matches = Arrays.asList(
                Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED),
                Match.reconstitute(2L, TOURNAMENT_ID, 3L, 4L, null, null, VALID_DATE.plusDays(1), "Stadium B", MatchStatus.SCHEDULED)
        );
        when(matchRepository.findAllByTournamentId(TOURNAMENT_ID)).thenReturn(matches);

        // When
        List<Match> result = service.getAllByTournamentId(TOURNAMENT_ID);

        // Then
        assertEquals(2, result.size());
        verify(matchRepository).findAllByTournamentId(TOURNAMENT_ID);
    }

    @Test
    @DisplayName("Should get all matches by team id")
    void shouldGetAllMatchesByTeamId() {
        // Given
        List<Match> matches = Arrays.asList(
                Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED),
                Match.reconstitute(2L, TOURNAMENT_ID, HOME_TEAM_ID, 3L, null, null, VALID_DATE.plusDays(1), "Stadium B", MatchStatus.SCHEDULED)
        );
        when(matchRepository.findAllByTeamId(HOME_TEAM_ID)).thenReturn(matches);

        // When
        List<Match> result = service.getAllByTeamId(HOME_TEAM_ID);

        // Then
        assertEquals(2, result.size());
        verify(matchRepository).findAllByTeamId(HOME_TEAM_ID);
    }
}
