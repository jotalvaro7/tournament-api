package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.UpdateMatchUseCase.UpdateMatchCommand;
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

@DisplayName("UpdateMatchService Tests")
@ExtendWith(MockitoExtension.class)
class UpdateMatchServiceTest {

    @Mock private MatchRepository matchRepository;

    private UpdateMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new UpdateMatchService(matchRepository);
    }

    @Test
    @DisplayName("Should update match successfully")
    void shouldUpdateMatchSuccessfully() {
        // Given
        Match existing = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED);
        LocalDateTime newDate = VALID_DATE.plusDays(1);
        UpdateMatchCommand command = new UpdateMatchCommand(MATCH_ID, newDate, "Stadium B");

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(existing));
        when(matchRepository.save(any(Match.class))).thenReturn(existing);

        // When
        Match result = service.update(command);

        // Then
        assertNotNull(result);
        verify(matchRepository).findById(MATCH_ID);
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match not found")
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        UpdateMatchCommand command = new UpdateMatchCommand(999L, VALID_DATE, "Stadium B");
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MatchNotFoundException.class, () -> service.update(command));
        verify(matchRepository, never()).save(any());
    }
}
