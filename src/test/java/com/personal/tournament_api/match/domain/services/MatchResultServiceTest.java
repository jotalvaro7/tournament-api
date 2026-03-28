package com.personal.tournament_api.match.domain.services;

import com.personal.tournament_api.match.domain.exceptions.InvalidMatchDataException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchResultOutcome;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("MatchResultService Unit Tests")
@ExtendWith(MockitoExtension.class)
class MatchResultServiceTest {

    @Mock private MatchTeamPort teamPort;

    private MatchResultService matchResultService;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);
    private static final Long TOURNAMENT_ID = 10L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;

    @BeforeEach
    void setUp() {
        matchResultService = new MatchResultService();
    }

    @Nested
    @DisplayName("Register New Result Tests")
    class RegisterNewResultTests {

        @Test
        @DisplayName("Should register new result and delegate stats to team port")
        void shouldRegisterNewResultAndDelegateStatsToTeamPort() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            MatchResultOutcome outcome = matchResultService.registerResult(match, teamPort, 3, 1);

            // Then
            assertFalse(outcome.isCorrection());
            assertEquals(3, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());
            assertEquals(MatchStatus.FINISHED, match.getStatus());

            verify(teamPort).validateBelongsToTournament(HOME_TEAM_ID, TOURNAMENT_ID);
            verify(teamPort).validateBelongsToTournament(AWAY_TEAM_ID, TOURNAMENT_ID);
            verify(teamPort).recordMatchResult(HOME_TEAM_ID, 3, 1);
            verify(teamPort).recordMatchResult(AWAY_TEAM_ID, 1, 3);
            verify(teamPort, never()).reverseMatchResult(anyLong(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should register draw result")
        void shouldRegisterDrawResult() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            MatchResultOutcome outcome = matchResultService.registerResult(match, teamPort, 2, 2);

            // Then
            assertFalse(outcome.isCorrection());
            verify(teamPort).recordMatchResult(HOME_TEAM_ID, 2, 2);
            verify(teamPort).recordMatchResult(AWAY_TEAM_ID, 2, 2);
            verify(teamPort, never()).reverseMatchResult(anyLong(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should register result with zero scores")
        void shouldRegisterResultWithZeroScores() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            matchResultService.registerResult(match, teamPort, 0, 0);

            // Then
            verify(teamPort).recordMatchResult(HOME_TEAM_ID, 0, 0);
            verify(teamPort).recordMatchResult(AWAY_TEAM_ID, 0, 0);
        }
    }

    @Nested
    @DisplayName("Correct Result Tests")
    class CorrectResultTests {

        @Test
        @DisplayName("Should correct result reversing previous stats before applying new ones")
        void shouldCorrectResultReversingPreviousStats() {
            // Given — Match finished with draw 2-2
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, 2, 2,
                    VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // When — Correct to 3-1 (home victory)
            MatchResultOutcome outcome = matchResultService.registerResult(match, teamPort, 3, 1);

            // Then
            assertTrue(outcome.isCorrection());
            assertEquals(2, outcome.previousHomeScore());
            assertEquals(2, outcome.previousAwayScore());

            // First reverses previous draw stats
            verify(teamPort).reverseMatchResult(HOME_TEAM_ID, 2, 2);
            verify(teamPort).reverseMatchResult(AWAY_TEAM_ID, 2, 2);

            // Then applies new victory stats
            verify(teamPort).recordMatchResult(HOME_TEAM_ID, 3, 1);
            verify(teamPort).recordMatchResult(AWAY_TEAM_ID, 1, 3);
        }

        @Test
        @DisplayName("Should correct victory to defeat")
        void shouldCorrectVictoryToDefeat() {
            // Given — Match finished with 3-0 (home victory)
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, 3, 0,
                    VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // When — Correct to 0-2 (away victory)
            MatchResultOutcome outcome = matchResultService.registerResult(match, teamPort, 0, 2);

            // Then
            assertTrue(outcome.isCorrection());
            verify(teamPort).reverseMatchResult(HOME_TEAM_ID, 3, 0);
            verify(teamPort).reverseMatchResult(AWAY_TEAM_ID, 0, 3);
            verify(teamPort).recordMatchResult(HOME_TEAM_ID, 0, 2);
            verify(teamPort).recordMatchResult(AWAY_TEAM_ID, 2, 0);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should delegate tournament validation to team port")
        void shouldDelegateTournamentValidationToTeamPort() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            matchResultService.registerResult(match, teamPort, 3, 1);

            // Then
            verify(teamPort).validateBelongsToTournament(HOME_TEAM_ID, TOURNAMENT_ID);
            verify(teamPort).validateBelongsToTournament(AWAY_TEAM_ID, TOURNAMENT_ID);
        }

        @Test
        @DisplayName("Should propagate exception when team port throws validation error")
        void shouldPropagateExceptionWhenPortThrowsValidationError() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            doThrow(new InvalidMatchDataException("Home team does not belong to tournament"))
                    .when(teamPort).validateBelongsToTournament(HOME_TEAM_ID, TOURNAMENT_ID);

            // When & Then
            assertThrows(InvalidMatchDataException.class,
                    () -> matchResultService.registerResult(match, teamPort, 3, 1));

            verify(teamPort, never()).recordMatchResult(anyLong(), anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Prepare For Deletion Tests")
    class PrepareForDeletionTests {

        @Test
        @DisplayName("Should reverse match stats when match has result")
        void shouldReverseMatchStatsWhenMatchHasResult() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, 3, 1,
                    VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // When
            matchResultService.prepareMatchForDeletion(match, teamPort);

            // Then
            verify(teamPort).validateBelongsToTournament(HOME_TEAM_ID, TOURNAMENT_ID);
            verify(teamPort).validateBelongsToTournament(AWAY_TEAM_ID, TOURNAMENT_ID);
            verify(teamPort).reverseMatchResult(HOME_TEAM_ID, 3, 1);
            verify(teamPort).reverseMatchResult(AWAY_TEAM_ID, 1, 3);
        }

        @Test
        @DisplayName("Should do nothing when match has no result")
        void shouldDoNothingWhenMatchHasNoResult() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            matchResultService.prepareMatchForDeletion(match, teamPort);

            // Then
            verifyNoInteractions(teamPort);
        }
    }

    @Nested
    @DisplayName("Revert Result Tests")
    class RevertResultTests {

        @Test
        @DisplayName("Should revert match result stats")
        void shouldRevertMatchResultStats() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, 2, 0,
                    VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // When
            matchResultService.revertMatchResult(match, teamPort);

            // Then
            verify(teamPort).reverseMatchResult(HOME_TEAM_ID, 2, 0);
            verify(teamPort).reverseMatchResult(AWAY_TEAM_ID, 0, 2);
        }

        @Test
        @DisplayName("Should throw exception when reverting match without result")
        void shouldThrowExceptionWhenRevertingMatchWithoutResult() {
            // Given
            Match match = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When & Then
            assertThrows(InvalidMatchDataException.class,
                    () -> matchResultService.revertMatchResult(match, teamPort));

            verifyNoInteractions(teamPort);
        }
    }
}
