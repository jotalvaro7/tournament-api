package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase.FinishMatchCommand;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchResultOutcome;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("FinishMatchService Tests")
@ExtendWith(MockitoExtension.class)
class FinishMatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private MatchTeamPort matchTeamPort;
    @Mock private MatchResultService matchResultService;

    private FinishMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new FinishMatchService(matchRepository, matchTeamPort, matchResultService);
    }

    @Test
    @DisplayName("Should finish match with new result successfully")
    void shouldFinishMatchWithNewResultSuccessfully() {
        // Given
        Match match = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED);
        FinishMatchCommand command = new FinishMatchCommand(MATCH_ID, 3, 1);
        MatchResultOutcome outcome = MatchResultOutcome.newResult();

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchResultService.registerResult(match, matchTeamPort, 3, 1)).thenReturn(outcome);
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // When
        Match result = service.finishMatch(command);

        // Then
        assertNotNull(result);
        verify(matchResultService).registerResult(match, matchTeamPort, 3, 1);
        verify(matchRepository).save(match);
        verifyNoInteractions(matchTeamPort);
    }

    @Test
    @DisplayName("Should finish match with correction successfully")
    void shouldFinishMatchWithCorrectionSuccessfully() {
        // Given
        Match match = Match.reconstitute(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                2, 2, VALID_DATE, "Stadium A", MatchStatus.FINISHED);
        FinishMatchCommand command = new FinishMatchCommand(MATCH_ID, 3, 1);
        MatchResultOutcome outcome = MatchResultOutcome.correction(2, 2);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(matchResultService.registerResult(match, matchTeamPort, 3, 1)).thenReturn(outcome);
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // When
        Match result = service.finishMatch(command);

        // Then
        assertNotNull(result);
        verify(matchResultService).registerResult(match, matchTeamPort, 3, 1);
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match not found")
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        FinishMatchCommand command = new FinishMatchCommand(999L, 3, 1);
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MatchNotFoundException.class, () -> service.finishMatch(command));
        verifyNoInteractions(matchResultService);
        verifyNoInteractions(matchTeamPort);
    }
}
