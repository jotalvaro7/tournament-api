package com.personal.tournament_api.match.domain.model;

import com.personal.tournament_api.match.domain.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Match Domain Model Unit Tests")
class MatchTest {

    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @Nested
    @DisplayName("Constructor Validation Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create match with valid data")
        void shouldCreateMatchWithValidData() {
            // When
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // Then
            assertNotNull(match);
            assertEquals(1L, match.getId());
            assertEquals(10L, match.getTournamentId());
            assertEquals(1L, match.getHomeTeamId());
            assertEquals(2L, match.getAwayTeamId());
            assertEquals(VALID_DATE, match.getMatchDate());
            assertEquals("Stadium A", match.getField());
            assertEquals(MatchStatus.SCHEDULED, match.getStatus());
            assertNull(match.getHomeTeamScore());
            assertNull(match.getAwayTeamScore());
        }

        @Test
        @DisplayName("Should throw exception when tournament ID is null")
        void shouldThrowExceptionWhenTournamentIdIsNull() {
            assertThrows(InvalidMatchTournamentIdException.class, () ->
                Match.create(null, 1L, 2L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when tournament ID is zero")
        void shouldThrowExceptionWhenTournamentIdIsZero() {
            assertThrows(InvalidMatchTournamentIdException.class, () ->
                Match.create(0L, 1L, 2L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when tournament ID is negative")
        void shouldThrowExceptionWhenTournamentIdIsNegative() {
            assertThrows(InvalidMatchTournamentIdException.class, () ->
                Match.create(-1L, 1L, 2L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when home team ID is null")
        void shouldThrowExceptionWhenHomeTeamIdIsNull() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, null, 2L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when home team ID is zero")
        void shouldThrowExceptionWhenHomeTeamIdIsZero() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, 0L, 2L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when home team ID is negative")
        void shouldThrowExceptionWhenHomeTeamIdIsNegative() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, -1L, 2L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when away team ID is null")
        void shouldThrowExceptionWhenAwayTeamIdIsNull() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, 1L, null, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when away team ID is zero")
        void shouldThrowExceptionWhenAwayTeamIdIsZero() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, 1L, 0L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when away team ID is negative")
        void shouldThrowExceptionWhenAwayTeamIdIsNegative() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, 1L, -1L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when home and away team IDs are equal")
        void shouldThrowExceptionWhenTeamIdsAreEqual() {
            assertThrows(InvalidMatchTeamsException.class, () ->
                Match.create(10L, 1L, 1L, VALID_DATE, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when field is null")
        void shouldThrowExceptionWhenFieldIsNull() {
            assertThrows(InvalidMatchFieldException.class, () ->
                Match.create(10L, 1L, 2L, VALID_DATE, null, null)
            );
        }

        @Test
        @DisplayName("Should throw exception when field is empty")
        void shouldThrowExceptionWhenFieldIsEmpty() {
            assertThrows(InvalidMatchFieldException.class, () ->
                Match.create(10L, 1L, 2L, VALID_DATE, "", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when field is blank")
        void shouldThrowExceptionWhenFieldIsBlank() {
            assertThrows(InvalidMatchFieldException.class, () ->
                Match.create(10L, 1L, 2L, VALID_DATE, "   ", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when field exceeds 100 characters")
        void shouldThrowExceptionWhenFieldIsTooLong() {
            String longField = "A".repeat(101);
            assertThrows(InvalidMatchFieldException.class, () ->
                Match.create(10L, 1L, 2L, VALID_DATE, longField, null)
            );
        }

        @Test
        @DisplayName("Should create match when field is exactly 100 characters")
        void shouldCreateMatchWhenFieldIsMaximumLength() {
            String maxField = "A".repeat(100);
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, maxField, MatchStatus.SCHEDULED, null);
            assertNotNull(match);
            assertEquals(maxField, match.getField());
        }

        @Test
        @DisplayName("Should throw exception when match date is null")
        void shouldThrowExceptionWhenMatchDateIsNull() {
            assertThrows(InvalidMatchDateException.class, () ->
                Match.create(10L, 1L, 2L, null, "Stadium A", null)
            );
        }

        @Test
        @DisplayName("Should create match with persistence constructor")
        void shouldCreateMatchWithPersistenceConstructor() {
            // When
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, 3, 1,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // Then
            assertNotNull(match);
            assertEquals(1L, match.getId());
            assertEquals(10L, match.getTournamentId());
            assertEquals(1L, match.getHomeTeamId());
            assertEquals(2L, match.getAwayTeamId());
            assertEquals(3, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());
            assertEquals(VALID_DATE, match.getMatchDate());
            assertEquals("Stadium A", match.getField());
            assertEquals(MatchStatus.FINISHED, match.getStatus());
        }
    }

    @Nested
    @DisplayName("Set Match Result Tests")
    class SetMatchResultTests {

        @Test
        @DisplayName("Should set match result for scheduled match")
        void shouldSetMatchResultForScheduledMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            MatchResultOutcome outcome = match.setMatchResult(3, 1);

            // Then
            assertEquals(3, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());
            assertEquals(MatchStatus.FINISHED, match.getStatus());
            assertFalse(outcome.isCorrection());
        }

        @Test
        @DisplayName("Should correct match result for finished match")
        void shouldCorrectMatchResultForFinishedMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, 2, 2,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // When
            MatchResultOutcome outcome = match.setMatchResult(3, 1);

            // Then
            assertEquals(3, match.getHomeTeamScore());
            assertEquals(1, match.getAwayTeamScore());
            assertEquals(MatchStatus.FINISHED, match.getStatus());
            assertTrue(outcome.isCorrection());
            assertEquals(2, outcome.previousHomeScore());
            assertEquals(2, outcome.previousAwayScore());
        }

        @Test
        @DisplayName("Should set result with zero scores")
        void shouldSetResultWithZeroScores() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            MatchResultOutcome outcome = match.setMatchResult(0, 0);

            // Then
            assertEquals(0, match.getHomeTeamScore());
            assertEquals(0, match.getAwayTeamScore());
            assertEquals(MatchStatus.FINISHED, match.getStatus());
            assertFalse(outcome.isCorrection());
        }

        @Test
        @DisplayName("Should throw exception when setting result with negative home score")
        void shouldThrowExceptionWhenHomeScoreIsNegative() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            assertThrows(InvalidMatchScoreException.class, () ->
                match.setMatchResult(-1, 0)
            );
        }

        @Test
        @DisplayName("Should throw exception when setting result with negative away score")
        void shouldThrowExceptionWhenAwayScoreIsNegative() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            assertThrows(InvalidMatchScoreException.class, () ->
                match.setMatchResult(0, -1)
            );
        }

        @Test
        @DisplayName("Should throw exception when setting result for postponed match")
        void shouldThrowExceptionWhenSettingResultForPostponedMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            match.postponeMatch();

            // When & Then
            assertThrows(InvalidMatchStatusTransitionException.class, () ->
                match.setMatchResult(3, 1)
            );
        }
    }

    @Nested
    @DisplayName("Postpone Match Tests")
    class PostponeMatchTests {

        @Test
        @DisplayName("Should postpone scheduled match")
        void shouldPostponeScheduledMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When
            match.postponeMatch();

            // Then
            assertEquals(MatchStatus.POSTPONED, match.getStatus());
        }

        @Test
        @DisplayName("Should throw exception when postponing finished match")
        void shouldThrowExceptionWhenPostponingFinishedMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, 3, 1,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);

            // When & Then
            assertThrows(InvalidMatchStatusTransitionException.class, () ->
                match.postponeMatch()
            );
        }
    }

    @Nested
    @DisplayName("Update Match Details Tests")
    class UpdateMatchDetailsTests {

        @Test
        @DisplayName("Should update match details for scheduled match")
        void shouldUpdateMatchDetailsForScheduledMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            LocalDateTime newDate = VALID_DATE.plusDays(1);

            // When
            match.updateMatchDetails(newDate, "Stadium B", null);

            // Then
            assertEquals(newDate, match.getMatchDate());
            assertEquals("Stadium B", match.getField());
        }

        @Test
        @DisplayName("Should throw exception when updating finished match")
        void shouldThrowExceptionWhenUpdatingFinishedMatch() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, 3, 1,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);
            LocalDateTime newDate = VALID_DATE.plusDays(1);

            // When & Then
            assertThrows(InvalidMatchStatusTransitionException.class, () ->
                match.updateMatchDetails(newDate, "Stadium B", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when updating with invalid field")
        void shouldThrowExceptionWhenUpdatingWithInvalidField() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When & Then
            assertThrows(InvalidMatchFieldException.class, () ->
                match.updateMatchDetails(VALID_DATE, "", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when updating with null date")
        void shouldThrowExceptionWhenUpdatingWithNullDate() {
            // Given
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            // When & Then
            assertThrows(InvalidMatchDateException.class, () ->
                match.updateMatchDetails(null, "Stadium B", null)
            );
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all properties are equal")
        void shouldBeEqualWhenAllPropertiesAreEqual() {
            Match match1 = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            Match match2 = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            assertEquals(match1, match2);
            assertEquals(match1.hashCode(), match2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when IDs differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            Match match1 = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            Match match2 = Match.reconstitute(2L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            assertNotEquals(match1, match2);
        }

        @Test
        @DisplayName("Should not be equal when tournament IDs differ")
        void shouldNotBeEqualWhenTournamentIdsDiffer() {
            Match match1 = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            Match match2 = Match.reconstitute(1L, 11L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            assertNotEquals(match1, match2);
        }

        @Test
        @DisplayName("Should not be equal when home team IDs differ")
        void shouldNotBeEqualWhenHomeTeamIdsDiffer() {
            Match match1 = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            Match match2 = Match.reconstitute(1L, 10L, 3L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

            assertNotEquals(match1, match2);
        }

        @Test
        @DisplayName("Should not be equal when compared to null")
        void shouldNotBeEqualWhenComparedToNull() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            assertNotEquals(match, null);
        }

        @Test
        @DisplayName("Should not be equal when compared to different class")
        void shouldNotBeEqualWhenComparedToDifferentClass() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            assertNotEquals(match, "Not a Match");
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            assertEquals(match, match);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain all properties in toString")
        void shouldContainAllPropertiesInToString() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);
            String toString = match.toString();

            assertTrue(toString.contains("id=1"));
            assertTrue(toString.contains("tournamentId=10"));
            assertTrue(toString.contains("homeTeamId=1"));
            assertTrue(toString.contains("awayTeamId=2"));
            assertTrue(toString.contains("Stadium A"));
            assertTrue(toString.contains("SCHEDULED"));
        }

        @Test
        @DisplayName("Should contain scores in toString when match is finished")
        void shouldContainScoresInToStringWhenFinished() {
            Match match = Match.reconstitute(1L, 10L, 1L, 2L, 3, 1,
                VALID_DATE, "Stadium A", MatchStatus.FINISHED, null);
            String toString = match.toString();

            assertTrue(toString.contains("homeTeamScore=3"));
            assertTrue(toString.contains("awayTeamScore=1"));
            assertTrue(toString.contains("FINISHED"));
        }
    }
}
