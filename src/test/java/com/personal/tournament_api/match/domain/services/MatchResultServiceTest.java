package com.personal.tournament_api.match.domain.services;

import com.personal.tournament_api.match.domain.exceptions.InvalidMatchDataException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchResultOutcome;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.team.domain.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MatchResultService Unit Tests")
class MatchResultServiceTest {

    private MatchResultService matchResultService;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);
    private static final Long TOURNAMENT_ID = 10L;

    @BeforeEach
    void setUp() {
        matchResultService = new MatchResultService();
    }

    @Nested
    @DisplayName("Register New Result Tests")
    class RegisterNewResultTests {

        @Test
        @DisplayName("Should register new result and update teams statistics")
        void shouldRegisterNewResultAndUpdateTeamsStatistics() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // When
            MatchResultOutcome outcome = matchResultService.registerResult(
                match, homeTeam, awayTeam, 3, 1
            );

            // Then
            assertFalse(outcome.isCorrection());
            assertEquals(3, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());
            assertEquals(MatchStatus.FINISHED, match.getStatus());

            // Home team (victory 3-1)
            assertEquals(3, homeTeam.getPoints());
            assertEquals(1, homeTeam.getMatchesPlayed());
            assertEquals(1, homeTeam.getMatchesWin());
            assertEquals(0, homeTeam.getMatchesDraw());
            assertEquals(0, homeTeam.getMatchesLost());
            assertEquals(3, homeTeam.getGoalsFor());
            assertEquals(1, homeTeam.getGoalsAgainst());
            assertEquals(2, homeTeam.getGoalDifference());

            // Away team (defeat 1-3)
            assertEquals(0, awayTeam.getPoints());
            assertEquals(1, awayTeam.getMatchesPlayed());
            assertEquals(0, awayTeam.getMatchesWin());
            assertEquals(0, awayTeam.getMatchesDraw());
            assertEquals(1, awayTeam.getMatchesLost());
            assertEquals(1, awayTeam.getGoalsFor());
            assertEquals(3, awayTeam.getGoalsAgainst());
            assertEquals(-2, awayTeam.getGoalDifference());
        }

        @Test
        @DisplayName("Should register draw result and update teams statistics")
        void shouldRegisterDrawResultAndUpdateTeamsStatistics() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // When
            MatchResultOutcome outcome = matchResultService.registerResult(
                match, homeTeam, awayTeam, 2, 2
            );

            // Then
            assertFalse(outcome.isCorrection());
            assertEquals(2, match.getHomeTeamScore());
            assertEquals(2, match.getAwayTeamScore());

            // Home team (draw 2-2)
            assertEquals(1, homeTeam.getPoints());
            assertEquals(0, homeTeam.getMatchesWin());
            assertEquals(1, homeTeam.getMatchesDraw());
            assertEquals(0, homeTeam.getMatchesLost());

            // Away team (draw 2-2)
            assertEquals(1, awayTeam.getPoints());
            assertEquals(0, awayTeam.getMatchesWin());
            assertEquals(1, awayTeam.getMatchesDraw());
            assertEquals(0, awayTeam.getMatchesLost());
        }

        @Test
        @DisplayName("Should register result with zero scores")
        void shouldRegisterResultWithZeroScores() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // When
            MatchResultOutcome outcome = matchResultService.registerResult(
                match, homeTeam, awayTeam, 0, 0
            );

            // Then
            assertFalse(outcome.isCorrection());
            assertEquals(0, match.getHomeTeamScore());
            assertEquals(0, match.getAwayTeamScore());
            assertEquals(1, homeTeam.getPoints());
            assertEquals(1, awayTeam.getPoints());
        }
    }

    @Nested
    @DisplayName("Correct Result Tests")
    class CorrectResultTests {

        @Test
        @DisplayName("Should correct result and reverse previous statistics")
        void shouldCorrectResultAndReversePreviousStatistics() {
            // Given - Match finished with 2-2 (draw)
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, 2, 2,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED);
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID,
                1, 1, 0, 1, 0, 2, 2, 0); // 1 point, 1 draw
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID,
                1, 1, 0, 1, 0, 2, 2, 0); // 1 point, 1 draw

            // When - Correct to 3-1 (home victory)
            MatchResultOutcome outcome = matchResultService.registerResult(
                match, homeTeam, awayTeam, 3, 1
            );

            // Then
            assertTrue(outcome.isCorrection());
            assertEquals(2, outcome.previousHomeScore());
            assertEquals(2, outcome.previousAwayScore());
            assertEquals(3, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());

            // Home team: reversed draw (1,2,2) → applied victory (3,3,1) = 3pts, 1W, 0D
            assertEquals(3, homeTeam.getPoints());
            assertEquals(1, homeTeam.getMatchesPlayed());
            assertEquals(1, homeTeam.getMatchesWin());
            assertEquals(0, homeTeam.getMatchesDraw());
            assertEquals(0, homeTeam.getMatchesLost());
            assertEquals(3, homeTeam.getGoalsFor());
            assertEquals(1, homeTeam.getGoalsAgainst());
            assertEquals(2, homeTeam.getGoalDifference());

            // Away team: reversed draw (1,2,2) → applied defeat (0,1,3) = 0pts, 0W, 1L
            assertEquals(0, awayTeam.getPoints());
            assertEquals(1, awayTeam.getMatchesPlayed());
            assertEquals(0, awayTeam.getMatchesWin());
            assertEquals(0, awayTeam.getMatchesDraw());
            assertEquals(1, awayTeam.getMatchesLost());
            assertEquals(1, awayTeam.getGoalsFor());
            assertEquals(3, awayTeam.getGoalsAgainst());
            assertEquals(-2, awayTeam.getGoalDifference());
        }

        @Test
        @DisplayName("Should correct victory to defeat")
        void shouldCorrectVictoryToDefeat() {
            // Given - Match finished with 3-0 (home victory)
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, 3, 0,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED);
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID,
                3, 1, 1, 0, 0, 3, 0, 3);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID,
                0, 1, 0, 0, 1, 0, 3, -3);

            // When - Correct to 0-2 (away victory)
            MatchResultOutcome outcome = matchResultService.registerResult(
                match, homeTeam, awayTeam, 0, 2
            );

            // Then
            assertTrue(outcome.isCorrection());
            assertEquals(3, outcome.previousHomeScore());
            assertEquals(0, outcome.previousAwayScore());

            // Home team: now has defeat
            assertEquals(0, homeTeam.getPoints());
            assertEquals(0, homeTeam.getMatchesWin());
            assertEquals(1, homeTeam.getMatchesLost());
            assertEquals(0, homeTeam.getGoalsFor());
            assertEquals(2, homeTeam.getGoalsAgainst());

            // Away team: now has victory
            assertEquals(3, awayTeam.getPoints());
            assertEquals(1, awayTeam.getMatchesWin());
            assertEquals(0, awayTeam.getMatchesLost());
            assertEquals(2, awayTeam.getGoalsFor());
            assertEquals(0, awayTeam.getGoalsAgainst());
        }

        @Test
        @DisplayName("Should correct multiple times maintaining consistency")
        void shouldCorrectMultipleTimesMaintainingConsistency() {
            // Given - First result 2-1
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // First result: 2-1
            matchResultService.registerResult(match, homeTeam, awayTeam, 2, 1);
            assertEquals(3, homeTeam.getPoints());
            assertEquals(0, awayTeam.getPoints());

            // First correction: 2-2
            matchResultService.registerResult(match, homeTeam, awayTeam, 2, 2);
            assertEquals(1, homeTeam.getPoints());
            assertEquals(1, awayTeam.getPoints());

            // Second correction: 1-3
            MatchResultOutcome outcome = matchResultService.registerResult(
                match, homeTeam, awayTeam, 1, 3
            );

            // Then
            assertTrue(outcome.isCorrection());
            assertEquals(0, homeTeam.getPoints());
            assertEquals(1, homeTeam.getMatchesLost());
            assertEquals(3, awayTeam.getPoints());
            assertEquals(1, awayTeam.getMatchesWin());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw exception when home team does not belong to tournament")
        void shouldThrowExceptionWhenHomeTeamDoesNotBelongToTournament() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", 99L); // Wrong tournament
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // When & Then
            InvalidMatchDataException exception = assertThrows(
                InvalidMatchDataException.class,
                () -> matchResultService.registerResult(match, homeTeam, awayTeam, 3, 1)
            );

            assertTrue(exception.getMessage().contains("Home team"));
            assertTrue(exception.getMessage().contains("1"));
            assertTrue(exception.getMessage().contains(TOURNAMENT_ID.toString()));
        }

        @Test
        @DisplayName("Should throw exception when away team does not belong to tournament")
        void shouldThrowExceptionWhenAwayTeamDoesNotBelongToTournament() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", 99L); // Wrong tournament

            // When & Then
            InvalidMatchDataException exception = assertThrows(
                InvalidMatchDataException.class,
                () -> matchResultService.registerResult(match, homeTeam, awayTeam, 3, 1)
            );

            assertTrue(exception.getMessage().contains("Away team"));
            assertTrue(exception.getMessage().contains("2"));
            assertTrue(exception.getMessage().contains(TOURNAMENT_ID.toString()));
        }

        @Test
        @DisplayName("Should throw exception when both teams do not belong to tournament")
        void shouldThrowExceptionWhenBothTeamsDoNotBelongToTournament() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", 88L);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", 99L);

            // When & Then - Should fail on first validation (home team)
            InvalidMatchDataException exception = assertThrows(
                InvalidMatchDataException.class,
                () -> matchResultService.registerResult(match, homeTeam, awayTeam, 3, 1)
            );

            assertTrue(exception.getMessage().contains("Home team"));
        }

        @Test
        @DisplayName("Should not throw exception when both teams belong to tournament")
        void shouldNotThrowExceptionWhenBothTeamsBelongToTournament() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // When & Then
            assertDoesNotThrow(() ->
                matchResultService.registerResult(match, homeTeam, awayTeam, 3, 1)
            );
        }
    }

    @Nested
    @DisplayName("Complex Scenario Tests")
    class ComplexScenarioTests {

        @Test
        @DisplayName("Should handle high score result correctly")
        void shouldHandleHighScoreResultCorrectly() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, VALID_DATE, "Stadium A");
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID);

            // When
            matchResultService.registerResult(match, homeTeam, awayTeam, 10, 8);

            // Then
            assertEquals(10, match.getHomeTeamScore());
            assertEquals(8, match.getAwayTeamScore());
            assertEquals(3, homeTeam.getPoints());
            assertEquals(10, homeTeam.getGoalsFor());
            assertEquals(8, homeTeam.getGoalsAgainst());
            assertEquals(2, homeTeam.getGoalDifference());
        }

        @Test
        @DisplayName("Should handle result correction from high score to low score")
        void shouldHandleResultCorrectionFromHighScoreToLowScore() {
            // Given
            Match match = new Match(1L, TOURNAMENT_ID, 1L, 2L, 5, 3,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED);
            Team homeTeam = new Team(1L, "Home Team", "Coach A", TOURNAMENT_ID,
                3, 1, 1, 0, 0, 5, 3, 2);
            Team awayTeam = new Team(2L, "Away Team", "Coach B", TOURNAMENT_ID,
                0, 1, 0, 0, 1, 3, 5, -2);

            // When
            matchResultService.registerResult(match, homeTeam, awayTeam, 0, 1);

            // Then
            assertEquals(0, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());
            assertEquals(0, homeTeam.getPoints());
            assertEquals(1, homeTeam.getMatchesLost());
            assertEquals(0, homeTeam.getGoalsFor());
            assertEquals(1, homeTeam.getGoalsAgainst());
            assertEquals(3, awayTeam.getPoints());
            assertEquals(1, awayTeam.getMatchesWin());
            assertEquals(1, awayTeam.getGoalsFor());
            assertEquals(0, awayTeam.getGoalsAgainst());
        }
    }
}
