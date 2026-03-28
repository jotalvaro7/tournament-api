package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("PostponeMatchService Tests")
@ExtendWith(MockitoExtension.class)
class PostponeMatchServiceTest {

    @Mock private MatchRepository matchRepository;

    private PostponeMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new PostponeMatchService(matchRepository);
    }

    @Test
    @DisplayName("Should postpone match successfully")
    void shouldPostponeMatchSuccessfully() {
        // Given
        Match match = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // When
        Match result = service.postponeMatch(MATCH_ID);

        // Then
        assertNotNull(result);
        assertEquals(MatchStatus.POSTPONED, result.getStatus());
        verify(matchRepository).findById(MATCH_ID);
        verify(matchRepository).save(match);
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match not found")
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MatchNotFoundException.class, () -> service.postponeMatch(999L));
        verify(matchRepository, never()).save(any());
    }
}
