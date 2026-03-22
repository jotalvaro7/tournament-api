package com.personal.tournament_api.team.domain.model;

import com.personal.tournament_api.team.domain.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Team Domain Model Unit Tests")
class TeamTest {

    @Nested
    @DisplayName("Constructor Validation Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create team with valid data")
        void shouldCreateTeamWithValidData() {
            // When
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // Then
            assertNotNull(team);
            assertEquals(1L, team.getId());
            assertEquals("Real Madrid", team.getName());
            assertEquals("Carlo Ancelotti", team.getCoach());
            assertEquals(10L, team.getTournamentId());
            assertEquals(0, team.getPoints());
            assertEquals(0, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
            assertEquals(0, team.getGoalsFor());
            assertEquals(0, team.getGoalsAgainst());
            assertEquals(0, team.getGoalDifference());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            assertThrows(InvalidTeamNameException.class, () -> {
                Team.reconstitute(1L, null, "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {
            assertThrows(InvalidTeamNameException.class, () -> {
                Team.reconstitute(1L, "", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            assertThrows(InvalidTeamNameException.class, () -> {
                Team.reconstitute(1L, "   ", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when name is too short")
        void shouldThrowExceptionWhenNameIsTooShort() {
            assertThrows(InvalidTeamNameException.class, () -> {
                Team.reconstitute(1L, "AB", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when name is too long")
        void shouldThrowExceptionWhenNameIsTooLong() {
            String longName = "A".repeat(101);
            assertThrows(InvalidTeamNameException.class, () -> {
                Team.reconstitute(1L, longName, "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should create team when name is exactly 3 characters")
        void shouldCreateTeamWhenNameIsMinimumLength() {
            Team team = Team.reconstitute(1L, "FCB", "Xavi Hernandez", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            assertNotNull(team);
            assertEquals("FCB", team.getName());
        }

        @Test
        @DisplayName("Should create team when name is exactly 100 characters")
        void shouldCreateTeamWhenNameIsMaximumLength() {
            String maxName = "A".repeat(100);
            Team team = Team.reconstitute(1L, maxName, "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            assertNotNull(team);
            assertEquals(maxName, team.getName());
        }

        @Test
        @DisplayName("Should throw exception when coach is null")
        void shouldThrowExceptionWhenCoachIsNull() {
            assertThrows(InvalidTeamCoachException.class, () -> {
                Team.reconstitute(1L, "Real Madrid", null, 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when coach is empty")
        void shouldThrowExceptionWhenCoachIsEmpty() {
            assertThrows(InvalidTeamCoachException.class, () -> {
                Team.reconstitute(1L, "Real Madrid", "", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when coach is blank")
        void shouldThrowExceptionWhenCoachIsBlank() {
            assertThrows(InvalidTeamCoachException.class, () -> {
                Team.reconstitute(1L, "Real Madrid", "   ", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when coach is too short")
        void shouldThrowExceptionWhenCoachIsTooShort() {
            assertThrows(InvalidTeamCoachException.class, () -> {
                Team.reconstitute(1L, "Real Madrid", "AB", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when coach is too long")
        void shouldThrowExceptionWhenCoachIsTooLong() {
            String longCoach = "A".repeat(101);
            assertThrows(InvalidTeamCoachException.class, () -> {
                Team.reconstitute(1L, "Real Madrid", longCoach, 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            });
        }

        @Test
        @DisplayName("Should create team when coach is exactly 3 characters")
        void shouldCreateTeamWhenCoachIsMinimumLength() {
            Team team = Team.reconstitute(1L, "Real Madrid", "Zzi", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            assertNotNull(team);
            assertEquals("Zzi", team.getCoach());
        }

        @Test
        @DisplayName("Should create team when coach is exactly 100 characters")
        void shouldCreateTeamWhenCoachIsMaximumLength() {
            String maxCoach = "A".repeat(100);
            Team team = Team.reconstitute(1L, "Real Madrid", maxCoach, 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            assertNotNull(team);
            assertEquals(maxCoach, team.getCoach());
        }

        @Test
        @DisplayName("Should throw exception when tournamentId is null")
        void shouldThrowExceptionWhenTournamentIdIsNull() {
            assertThrows(InvalidTeamTournamentIdException.class, () ->
                Team.create("Real Madrid", "Carlo Ancelotti", null)
            );
        }

        @Test
        @DisplayName("Should throw exception when tournamentId is zero")
        void shouldThrowExceptionWhenTournamentIdIsZero() {
            assertThrows(InvalidTeamTournamentIdException.class, () ->
                Team.create("Real Madrid", "Carlo Ancelotti", 0L)
            );
        }

        @Test
        @DisplayName("Should throw exception when tournamentId is negative")
        void shouldThrowExceptionWhenTournamentIdIsNegative() {
            assertThrows(InvalidTeamTournamentIdException.class, () ->
                Team.create("Real Madrid", "Carlo Ancelotti", -1L)
            );
        }
    }

    @Nested
    @DisplayName("Update Details Tests")
    class UpdateDetailsTests {

        @Test
        @DisplayName("Should update name and coach with valid data")
        void shouldUpdateDetailsWithValidData() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.updateDetails("Real Madrid CF", "Zinedine Zidane");

            // Then
            assertEquals("Real Madrid CF", team.getName());
            assertEquals("Zinedine Zidane", team.getCoach());
        }

        @Test
        @DisplayName("Should throw exception when updating with invalid name")
        void shouldThrowExceptionWhenUpdatingWithInvalidName() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamNameException.class, () -> {
                team.updateDetails("", "Carlo Ancelotti");
            });
        }

        @Test
        @DisplayName("Should throw exception when updating with invalid coach")
        void shouldThrowExceptionWhenUpdatingWithInvalidCoach() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamCoachException.class, () -> {
                team.updateDetails("Real Madrid", "");
            });
        }

        @Test
        @DisplayName("Should maintain statistics after update")
        void shouldMaintainStatisticsAfterUpdate() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            team.registerVictory(3, 1);

            // When
            team.updateDetails("Real Madrid CF", "Zinedine Zidane");

            // Then
            assertEquals("Real Madrid CF", team.getName());
            assertEquals("Zinedine Zidane", team.getCoach());
            assertEquals(3, team.getPoints());
            assertEquals(1, team.getMatchesWin());
        }
    }

    @Nested
    @DisplayName("Register Victory Tests")
    class RegisterVictoryTests {

        @Test
        @DisplayName("Should register victory and update statistics correctly")
        void shouldRegisterVictoryAndUpdateStatistics() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerVictory(3, 1);

            // Then
            assertEquals(3, team.getPoints());
            assertEquals(1, team.getMatchesPlayed());
            assertEquals(1, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
            assertEquals(3, team.getGoalsFor());
            assertEquals(1, team.getGoalsAgainst());
            assertEquals(2, team.getGoalDifference());
        }

        @Test
        @DisplayName("Should accumulate multiple victories correctly")
        void shouldAccumulateMultipleVictories() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerVictory(3, 1);
            team.registerVictory(2, 0);
            team.registerVictory(1, 0);

            // Then
            assertEquals(9, team.getPoints()); // 3 + 3 + 3
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(3, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
            assertEquals(6, team.getGoalsFor()); // 3 + 2 + 1
            assertEquals(1, team.getGoalsAgainst()); // 1 + 0 + 0
            assertEquals(5, team.getGoalDifference()); // 6 - 1
        }

        @Test
        @DisplayName("Should throw exception when goalsFor is negative in victory")
        void shouldThrowExceptionWhenGoalsForIsNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.registerVictory(-1, 0);
            });
        }

        @Test
        @DisplayName("Should throw exception when goalsAgainst is negative in victory")
        void shouldThrowExceptionWhenGoalsAgainstIsNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.registerVictory(3, -1);
            });
        }

        @Test
        @DisplayName("Should register victory with zero goals against")
        void shouldRegisterVictoryWithZeroGoalsAgainst() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerVictory(1, 0);

            // Then
            assertEquals(3, team.getPoints());
            assertEquals(1, team.getGoalsFor());
            assertEquals(0, team.getGoalsAgainst());
            assertEquals(1, team.getGoalDifference());
        }
    }

    @Nested
    @DisplayName("Register Draw Tests")
    class RegisterDrawTests {

        @Test
        @DisplayName("Should register draw and update statistics correctly")
        void shouldRegisterDrawAndUpdateStatistics() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDraw(1, 1);

            // Then
            assertEquals(1, team.getPoints());
            assertEquals(1, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(1, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
            assertEquals(1, team.getGoalsFor());
            assertEquals(1, team.getGoalsAgainst());
            assertEquals(0, team.getGoalDifference());
        }

        @Test
        @DisplayName("Should accumulate multiple draws correctly")
        void shouldAccumulateMultipleDraws() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDraw(1, 1);
            team.registerDraw(2, 2);
            team.registerDraw(0, 0);

            // Then
            assertEquals(3, team.getPoints()); // 1 + 1 + 1
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(3, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
            assertEquals(3, team.getGoalsFor()); // 1 + 2 + 0
            assertEquals(3, team.getGoalsAgainst()); // 1 + 2 + 0
            assertEquals(0, team.getGoalDifference()); // 3 - 3
        }

        @Test
        @DisplayName("Should throw exception when goalsFor is negative in draw")
        void shouldThrowExceptionWhenGoalsForIsNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.registerDraw(-1, -1);
            });
        }

        @Test
        @DisplayName("Should throw exception when goalsAgainst is negative in draw")
        void shouldThrowExceptionWhenGoalsAgainstIsNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.registerDraw(1, -1);
            });
        }

        @Test
        @DisplayName("Should register draw with zero goals")
        void shouldRegisterDrawWithZeroGoals() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDraw(0, 0);

            // Then
            assertEquals(1, team.getPoints());
            assertEquals(1, team.getMatchesDraw());
            assertEquals(0, team.getGoalsFor());
            assertEquals(0, team.getGoalsAgainst());
            assertEquals(0, team.getGoalDifference());
        }
    }

    @Nested
    @DisplayName("Register Defeat Tests")
    class RegisterDefeatTests {

        @Test
        @DisplayName("Should register defeat and update statistics correctly")
        void shouldRegisterDefeatAndUpdateStatistics() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDefeat(1, 3);

            // Then
            assertEquals(0, team.getPoints());
            assertEquals(1, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(1, team.getMatchesLost());
            assertEquals(1, team.getGoalsFor());
            assertEquals(3, team.getGoalsAgainst());
            assertEquals(-2, team.getGoalDifference());
        }

        @Test
        @DisplayName("Should accumulate multiple defeats correctly")
        void shouldAccumulateMultipleDefeats() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDefeat(1, 3);
            team.registerDefeat(0, 2);
            team.registerDefeat(2, 5);

            // Then
            assertEquals(0, team.getPoints());
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(3, team.getMatchesLost());
            assertEquals(3, team.getGoalsFor()); // 1 + 0 + 2
            assertEquals(10, team.getGoalsAgainst()); // 3 + 2 + 5
            assertEquals(-7, team.getGoalDifference()); // 3 - 10
        }

        @Test
        @DisplayName("Should throw exception when goalsFor is negative in defeat")
        void shouldThrowExceptionWhenGoalsForIsNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.registerDefeat(-1, 3);
            });
        }

        @Test
        @DisplayName("Should throw exception when goalsAgainst is negative in defeat")
        void shouldThrowExceptionWhenGoalsAgainstIsNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.registerDefeat(1, -3);
            });
        }

        @Test
        @DisplayName("Should register defeat with zero goals scored")
        void shouldRegisterDefeatWithZeroGoalsScored() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDefeat(0, 1);

            // Then
            assertEquals(0, team.getPoints());
            assertEquals(1, team.getMatchesLost());
            assertEquals(0, team.getGoalsFor());
            assertEquals(1, team.getGoalsAgainst());
            assertEquals(-1, team.getGoalDifference());
        }
    }

    @Nested
    @DisplayName("Record Match Result Tests")
    class RecordMatchResultTests {

        @Test
        @DisplayName("Should record victory when goalsFor > goalsAgainst")
        void shouldRecordVictoryWhenGoalsForIsGreater() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.recordMatchResult(3, 1);

            // Then
            assertEquals(3, team.getPoints());
            assertEquals(1, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
        }

        @Test
        @DisplayName("Should record draw when goalsFor == goalsAgainst")
        void shouldRecordDrawWhenGoalsAreEqual() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.recordMatchResult(2, 2);

            // Then
            assertEquals(1, team.getPoints());
            assertEquals(0, team.getMatchesWin());
            assertEquals(1, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
        }

        @Test
        @DisplayName("Should record defeat when goalsFor < goalsAgainst")
        void shouldRecordDefeatWhenGoalsForIsLess() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.recordMatchResult(1, 3);

            // Then
            assertEquals(0, team.getPoints());
            assertEquals(0, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(1, team.getMatchesLost());
        }

        @Test
        @DisplayName("Should throw exception when goals are negative in recordMatchResult")
        void shouldThrowExceptionWhenGoalsAreNegative() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertThrows(InvalidTeamGoalsException.class, () -> {
                team.recordMatchResult(-1, 2);
            });
        }

        @Test
        @DisplayName("Should maintain consistency after recording match result")
        void shouldMaintainConsistencyAfterRecordingMatchResult() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.recordMatchResult(3, 1);
            team.recordMatchResult(1, 1);
            team.recordMatchResult(0, 2);

            // Then
            assertEquals(4, team.getPoints()); // 3 + 1 + 0
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(1, team.getMatchesWin());
            assertEquals(1, team.getMatchesDraw());
            assertEquals(1, team.getMatchesLost());
            assertEquals(4, team.getGoalsFor()); // 3 + 1 + 0
            assertEquals(4, team.getGoalsAgainst()); // 1 + 1 + 2
            assertEquals(0, team.getGoalDifference()); // 4 - 4

            // Verify consistency: matchesPlayed == sum of results
            assertEquals(team.getMatchesPlayed(),
                team.getMatchesWin() + team.getMatchesDraw() + team.getMatchesLost());

            // Verify consistency: goalDifference == goalsFor - goalsAgainst
            assertEquals(team.getGoalDifference(),
                team.getGoalsFor() - team.getGoalsAgainst());
        }
    }

    @Nested
    @DisplayName("Mixed Match Results Tests")
    class MixedMatchResultsTests {

        @Test
        @DisplayName("Should handle complex season with multiple results")
        void shouldHandleComplexSeasonWithMultipleResults() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When - Simulate a season with various results
            team.registerVictory(3, 0);  // W: 3 points
            team.registerVictory(2, 1);  // W: 3 points
            team.registerDraw(1, 1);     // D: 1 point
            team.registerDefeat(0, 2);   // L: 0 points
            team.registerVictory(4, 2);  // W: 3 points
            team.registerDraw(2, 2);     // D: 1 point

            // Then
            assertEquals(11, team.getPoints()); // 3+3+1+0+3+1
            assertEquals(6, team.getMatchesPlayed());
            assertEquals(3, team.getMatchesWin());
            assertEquals(2, team.getMatchesDraw());
            assertEquals(1, team.getMatchesLost());
            assertEquals(12, team.getGoalsFor()); // 3+2+1+0+4+2
            assertEquals(8, team.getGoalsAgainst()); // 0+1+1+2+2+2
            assertEquals(4, team.getGoalDifference()); // 12 - 8
        }

        @Test
        @DisplayName("Should handle team with only victories")
        void shouldHandleTeamWithOnlyVictories() {
            // Given
            Team team = Team.reconstitute(1L, "Barcelona", "Xavi Hernandez", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerVictory(3, 0);
            team.registerVictory(2, 1);
            team.registerVictory(1, 0);

            // Then
            assertEquals(9, team.getPoints());
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(3, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
        }

        @Test
        @DisplayName("Should handle team with only defeats")
        void shouldHandleTeamWithOnlyDefeats() {
            // Given
            Team team = Team.reconstitute(1L, "Sevilla", "Jose Luis Mendilibar", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDefeat(0, 3);
            team.registerDefeat(1, 2);
            team.registerDefeat(0, 1);

            // Then
            assertEquals(0, team.getPoints());
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(0, team.getMatchesDraw());
            assertEquals(3, team.getMatchesLost());
        }

        @Test
        @DisplayName("Should handle team with only draws")
        void shouldHandleTeamWithOnlyDraws() {
            // Given
            Team team = Team.reconstitute(1L, "Atletico Madrid", "Diego Simeone", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            team.registerDraw(0, 0);
            team.registerDraw(1, 1);
            team.registerDraw(2, 2);

            // Then
            assertEquals(3, team.getPoints());
            assertEquals(3, team.getMatchesPlayed());
            assertEquals(0, team.getMatchesWin());
            assertEquals(3, team.getMatchesDraw());
            assertEquals(0, team.getMatchesLost());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are equal")
        void shouldBeEqualWhenAllFieldsAreEqual() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertEquals(team1, team2);
            assertEquals(team1.hashCode(), team2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ids are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(2L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertNotEquals(team1, team2);
        }

        @Test
        @DisplayName("Should not be equal when names are different")
        void shouldNotBeEqualWhenNamesAreDifferent() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(1L, "Barcelona", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertNotEquals(team1, team2);
        }

        @Test
        @DisplayName("Should not be equal when comparing with null")
        void shouldNotBeEqualWhenComparingWithNull() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertNotEquals(null, team);
        }

        @Test
        @DisplayName("Should not be equal when statistics are different")
        void shouldNotBeEqualWhenStatisticsAreDifferent() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            team1.registerVictory(3, 1);

            // When & Then
            assertNotEquals(team1, team2);
        }

        @Test
        @DisplayName("Should be equal to itself (reflexivity)")
        void shouldBeEqualToItself() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertEquals(team, team);
        }

        @Test
        @DisplayName("Should not be equal when comparing with different class")
        void shouldNotBeEqualWhenComparingWithDifferentClass() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Object other = new Object();

            // When & Then
            assertNotEquals(team, other);
        }

        @Test
        @DisplayName("Should return consistent hashCode on multiple calls")
        void shouldReturnConsistentHashCode() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            int hashCode1 = team.hashCode();
            int hashCode2 = team.hashCode();

            // Then
            assertEquals(hashCode1, hashCode2);
        }

        @Test
        @DisplayName("Should not be equal when coaches are different")
        void shouldNotBeEqualWhenCoachesAreDifferent() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(1L, "Real Madrid", "Zinedine Zidane", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertNotEquals(team1, team2);
        }

        @Test
        @DisplayName("Should not be equal when tournament IDs are different")
        void shouldNotBeEqualWhenTournamentIdsAreDifferent() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 20L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertNotEquals(team1, team2);
        }

        @Test
        @DisplayName("Should have different hashCode when teams are not equal")
        void shouldHaveDifferentHashCodeWhenTeamsAreNotEqual() {
            // Given
            Team team1 = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            Team team2 = Team.reconstitute(2L, "Barcelona", "Xavi Hernandez", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When & Then
            assertNotEquals(team1.hashCode(), team2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return string representation with all fields")
        void shouldReturnStringWithAllFields() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            String result = team.toString();

            // Then
            assertNotNull(result);
            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("name='Real Madrid'"));
            assertTrue(result.contains("coach='Carlo Ancelotti'"));
            assertTrue(result.contains("tournamentId=10"));
            assertTrue(result.contains("points=0"));
            assertTrue(result.contains("matchesPlayed=0"));
        }

        @Test
        @DisplayName("Should return string representation with updated statistics")
        void shouldReturnStringWithUpdatedStatistics() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);
            team.registerVictory(3, 1);

            // When
            String result = team.toString();

            // Then
            assertNotNull(result);
            assertTrue(result.contains("points=3"));
            assertTrue(result.contains("matchesPlayed=1"));
        }

        @Test
        @DisplayName("Should return string starting with class name")
        void shouldReturnStringStartingWithClassName() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            String result = team.toString();

            // Then
            assertTrue(result.startsWith("Team{"));
            assertTrue(result.endsWith("}"));
        }

        @Test
        @DisplayName("Should return consistent string on multiple calls")
        void shouldReturnConsistentStringOnMultipleCalls() {
            // Given
            Team team = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 10L, 0, 0, 0, 0, 0, 0, 0, 0);

            // When
            String result1 = team.toString();
            String result2 = team.toString();

            // Then
            assertEquals(result1, result2);
        }
    }
}
