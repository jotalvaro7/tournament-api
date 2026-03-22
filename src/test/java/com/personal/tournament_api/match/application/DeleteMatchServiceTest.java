package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("DeleteMatchService Tests")
@ExtendWith(MockitoExtension.class)
class DeleteMatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private MatchTeamPort matchTeamPort;
    @Mock private MatchResultService matchResultService;

    private DeleteMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new DeleteMatchService(matchRepository, matchTeamPort, matchResultService);
    }

    @Test
    @DisplayName("Should delete match without result successfully")
    void shouldDeleteMatchWithoutResultSuccessfully() {
        // Given
        Match match = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        doNothing().when(matchRepository).deleteById(MATCH_ID);

        // When
        service.delete(MATCH_ID);

        // Then
        verify(matchResultService).prepareMatchForDeletion(match, matchTeamPort);
        verify(matchRepository).deleteById(MATCH_ID);
    }

    @Test
    @DisplayName("Should delete match with result and delegate stats reversion to service")
    void shouldDeleteMatchWithResultAndDelegateStatsReversion() {
        // Given
        Match matchWithResult = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                3, 1, VALID_DATE, "Stadium A", MatchStatus.FINISHED);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(matchWithResult));

        // When
        service.delete(MATCH_ID);

        // Then
        verify(matchResultService).prepareMatchForDeletion(matchWithResult, matchTeamPort);
        verify(matchRepository).deleteById(MATCH_ID);
        verifyNoInteractions(matchTeamPort);
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match not found")
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MatchNotFoundException.class, () -> service.delete(999L));
        verify(matchRepository, never()).deleteById(anyLong());
        verifyNoInteractions(matchResultService);
    }
}
