package com.personal.tournament_api.tournament.domain.model;

import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tournament Domain Model Unit Tests")
class TournamentTest {

    @Nested
    @DisplayName("Constructor Validation Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create tournament with valid data")
        void shouldCreateTournamentWithValidData() {
            // When
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // Then
            assertNotNull(tournament);
            assertEquals(1L, tournament.getId());
            assertEquals("La Liga", tournament.getName());
            assertEquals("Spanish Football Championship", tournament.getDescription());
            assertEquals(StatusTournament.CREATED, tournament.getStatus());
        }

        @Test
        @DisplayName("Should create tournament with CREATED status by default")
        void shouldCreateTournamentWithCreatedStatusByDefault() {
            // When
            Tournament tournament = new Tournament(1L, "Premier League", "English Football Championship");

            // Then
            assertEquals(StatusTournament.CREATED, tournament.getStatus());
            assertTrue(tournament.isCreated());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            assertThrows(InvalidTournamentNameException.class, () -> {
                new Tournament(1L, null, "Valid description");
            });
        }

        @Test
        @DisplayName("Should throw exception when name is empty")
        void shouldThrowExceptionWhenNameIsEmpty() {
            assertThrows(InvalidTournamentNameException.class, () -> {
                new Tournament(1L, "", "Valid description");
            });
        }

        @Test
        @DisplayName("Should throw exception when description is null")
        void shouldThrowExceptionWhenDescriptionIsNull() {
            assertThrows(InvalidTournamentDescriptionException.class, () -> {
                new Tournament(1L, "Valid Name", null);
            });
        }

        @Test
        @DisplayName("Should throw exception when description is empty")
        void shouldThrowExceptionWhenDescriptionIsEmpty() {
            assertThrows(InvalidTournamentDescriptionException.class, () -> {
                new Tournament(1L, "Valid Name", "");
            });
        }

        @Test
        @DisplayName("Should create tournament with default constructor")
        void shouldCreateTournamentWithDefaultConstructor() {
            // When
            Tournament tournament = new Tournament();

            // Then
            assertNotNull(tournament);
            assertEquals(StatusTournament.CREATED, tournament.getStatus());
        }
    }

    @Nested
    @DisplayName("Update Details Tests")
    class UpdateDetailsTests {

        @Test
        @DisplayName("Should update name and description with valid data")
        void shouldUpdateDetailsWithValidData() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            tournament.updateDetails("La Liga Santander", "Spanish Top Division Football");

            // Then
            assertEquals("La Liga Santander", tournament.getName());
            assertEquals("Spanish Top Division Football", tournament.getDescription());
        }

        @Test
        @DisplayName("Should throw exception when updating with invalid name")
        void shouldThrowExceptionWhenUpdatingWithInvalidName() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertThrows(InvalidTournamentNameException.class, () -> {
                tournament.updateDetails("", "Valid description");
            });
        }

        @Test
        @DisplayName("Should throw exception when updating with invalid description")
        void shouldThrowExceptionWhenUpdatingWithInvalidDescription() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertThrows(InvalidTournamentDescriptionException.class, () -> {
                tournament.updateDetails("Valid Name", "");
            });
        }

        @Test
        @DisplayName("Should maintain status after update")
        void shouldMaintainStatusAfterUpdate() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When
            tournament.updateDetails("La Liga Santander", "Spanish Top Division Football");

            // Then
            assertEquals("La Liga Santander", tournament.getName());
            assertEquals("Spanish Top Division Football", tournament.getDescription());
            assertEquals(StatusTournament.IN_PROGRESS, tournament.getStatus());
        }
    }

    @Nested
    @DisplayName("Start Tournament Tests")
    class StartTournamentTests {

        @Test
        @DisplayName("Should start tournament when status is CREATED")
        void shouldStartTournamentWhenStatusIsCreated() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            tournament.startTournament();

            // Then
            assertEquals(StatusTournament.IN_PROGRESS, tournament.getStatus());
            assertTrue(tournament.isInProgress());
        }

        @Test
        @DisplayName("Should throw exception when starting tournament not in CREATED status")
        void shouldThrowExceptionWhenStartingTournamentNotInCreatedStatus() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournament.startTournament();
            });
        }

        @Test
        @DisplayName("Should throw exception when starting completed tournament")
        void shouldThrowExceptionWhenStartingCompletedTournament() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournament.startTournament();
            });
        }
    }

    @Nested
    @DisplayName("End Tournament Tests")
    class EndTournamentTests {

        @Test
        @DisplayName("Should end tournament when status is IN_PROGRESS")
        void shouldEndTournamentWhenStatusIsInProgress() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When
            tournament.endTournament();

            // Then
            assertEquals(StatusTournament.COMPLETED, tournament.getStatus());
            assertTrue(tournament.isCompleted());
        }

        @Test
        @DisplayName("Should throw exception when ending tournament not in IN_PROGRESS status")
        void shouldThrowExceptionWhenEndingTournamentNotInProgress() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournament.endTournament();
            });
        }

        @Test
        @DisplayName("Should throw exception when ending already completed tournament")
        void shouldThrowExceptionWhenEndingAlreadyCompletedTournament() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournament.endTournament();
            });
        }
    }

    @Nested
    @DisplayName("Cancel Tournament Tests")
    class CancelTournamentTests {

        @Test
        @DisplayName("Should cancel tournament when status is CREATED")
        void shouldCancelTournamentWhenStatusIsCreated() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            tournament.cancelTournament();

            // Then
            assertEquals(StatusTournament.CANCELLED, tournament.getStatus());
            assertTrue(tournament.isCancelled());
        }

        @Test
        @DisplayName("Should cancel tournament when status is IN_PROGRESS")
        void shouldCancelTournamentWhenStatusIsInProgress() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When
            tournament.cancelTournament();

            // Then
            assertEquals(StatusTournament.CANCELLED, tournament.getStatus());
            assertTrue(tournament.isCancelled());
        }

        @Test
        @DisplayName("Should throw exception when cancelling completed tournament")
        void shouldThrowExceptionWhenCancellingCompletedTournament() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournament.cancelTournament();
            });
        }
    }

    @Nested
    @DisplayName("Delete Validation Tests")
    class DeleteValidationTests {

        @Test
        @DisplayName("Should allow deletion when tournament is CREATED")
        void shouldAllowDeletionWhenTournamentIsCreated() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertTrue(tournament.canBeDeleted());
            assertDoesNotThrow(() -> tournament.validateIfCanBeDeleted());
        }

        @Test
        @DisplayName("Should allow deletion when tournament is COMPLETED")
        void shouldAllowDeletionWhenTournamentIsCompleted() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            // When & Then
            assertTrue(tournament.canBeDeleted());
            assertDoesNotThrow(() -> tournament.validateIfCanBeDeleted());
        }

        @Test
        @DisplayName("Should allow deletion when tournament is CANCELLED")
        void shouldAllowDeletionWhenTournamentIsCancelled() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.cancelTournament();

            // When & Then
            assertTrue(tournament.canBeDeleted());
            assertDoesNotThrow(() -> tournament.validateIfCanBeDeleted());
        }

        @Test
        @DisplayName("Should not allow deletion when tournament is IN_PROGRESS")
        void shouldNotAllowDeletionWhenTournamentIsInProgress() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When & Then
            assertFalse(tournament.canBeDeleted());
            assertThrows(TournamentCannotBeDeletedException.class, () -> {
                tournament.validateIfCanBeDeleted();
            });
        }
    }

    @Nested
    @DisplayName("Status Check Tests")
    class StatusCheckTests {

        @Test
        @DisplayName("Should correctly identify CREATED status")
        void shouldCorrectlyIdentifyCreatedStatus() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertTrue(tournament.isCreated());
            assertFalse(tournament.isInProgress());
            assertFalse(tournament.isCompleted());
            assertFalse(tournament.isCancelled());
        }

        @Test
        @DisplayName("Should correctly identify IN_PROGRESS status")
        void shouldCorrectlyIdentifyInProgressStatus() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When & Then
            assertFalse(tournament.isCreated());
            assertTrue(tournament.isInProgress());
            assertFalse(tournament.isCompleted());
            assertFalse(tournament.isCancelled());
        }

        @Test
        @DisplayName("Should correctly identify COMPLETED status")
        void shouldCorrectlyIdentifyCompletedStatus() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            // When & Then
            assertFalse(tournament.isCreated());
            assertFalse(tournament.isInProgress());
            assertTrue(tournament.isCompleted());
            assertFalse(tournament.isCancelled());
        }

        @Test
        @DisplayName("Should correctly identify CANCELLED status")
        void shouldCorrectlyIdentifyCancelledStatus() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.cancelTournament();

            // When & Then
            assertFalse(tournament.isCreated());
            assertFalse(tournament.isInProgress());
            assertFalse(tournament.isCompleted());
            assertTrue(tournament.isCancelled());
        }
    }

    @Nested
    @DisplayName("State Transitions Tests")
    class StateTransitionsTests {

        @Test
        @DisplayName("Should transition from CREATED to IN_PROGRESS to COMPLETED")
        void shouldTransitionFromCreatedToInProgressToCompleted() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertTrue(tournament.isCreated());

            tournament.startTournament();
            assertTrue(tournament.isInProgress());

            tournament.endTournament();
            assertTrue(tournament.isCompleted());
        }

        @Test
        @DisplayName("Should transition from CREATED to CANCELLED")
        void shouldTransitionFromCreatedToCancelled() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            tournament.cancelTournament();

            // Then
            assertTrue(tournament.isCancelled());
        }

        @Test
        @DisplayName("Should transition from IN_PROGRESS to CANCELLED")
        void shouldTransitionFromInProgressToCancelled() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When
            tournament.cancelTournament();

            // Then
            assertTrue(tournament.isCancelled());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal when all fields are equal")
        void shouldBeEqualWhenAllFieldsAreEqual() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertEquals(tournament1, tournament2);
            assertEquals(tournament1.hashCode(), tournament2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when ids are different")
        void shouldNotBeEqualWhenIdsAreDifferent() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(2L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertNotEquals(tournament1, tournament2);
        }

        @Test
        @DisplayName("Should not be equal when names are different")
        void shouldNotBeEqualWhenNamesAreDifferent() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(1L, "Premier League", "Spanish Football Championship");

            // When & Then
            assertNotEquals(tournament1, tournament2);
        }

        @Test
        @DisplayName("Should not be equal when descriptions are different")
        void shouldNotBeEqualWhenDescriptionsAreDifferent() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(1L, "La Liga", "Different Description");

            // When & Then
            assertNotEquals(tournament1, tournament2);
        }

        @Test
        @DisplayName("Should not be equal when statuses are different")
        void shouldNotBeEqualWhenStatusesAreDifferent() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament2.startTournament();

            // When & Then
            assertNotEquals(tournament1, tournament2);
        }

        @Test
        @DisplayName("Should not be equal when comparing with null")
        void shouldNotBeEqualWhenComparingWithNull() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertNotEquals(null, tournament);
        }

        @Test
        @DisplayName("Should be equal to itself (reflexivity)")
        void shouldBeEqualToItself() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When & Then
            assertEquals(tournament, tournament);
        }

        @Test
        @DisplayName("Should not be equal when comparing with different class")
        void shouldNotBeEqualWhenComparingWithDifferentClass() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Object other = new Object();

            // When & Then
            assertNotEquals(tournament, other);
        }

        @Test
        @DisplayName("Should return consistent hashCode on multiple calls")
        void shouldReturnConsistentHashCode() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            int hashCode1 = tournament.hashCode();
            int hashCode2 = tournament.hashCode();

            // Then
            assertEquals(hashCode1, hashCode2);
        }

        @Test
        @DisplayName("Should have different hashCode when tournaments are not equal")
        void shouldHaveDifferentHashCodeWhenTournamentsAreNotEqual() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(2L, "Premier League", "English Football Championship");

            // When & Then
            assertNotEquals(tournament1.hashCode(), tournament2.hashCode());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should return string representation with all fields")
        void shouldReturnStringWithAllFields() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            String result = tournament.toString();

            // Then
            assertNotNull(result);
            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("name='La Liga'"));
            assertTrue(result.contains("description='Spanish Football Championship'"));
            assertTrue(result.contains("status=CREATED"));
        }

        @Test
        @DisplayName("Should return string representation with updated status")
        void shouldReturnStringWithUpdatedStatus() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            // When
            String result = tournament.toString();

            // Then
            assertNotNull(result);
            assertTrue(result.contains("status=IN_PROGRESS"));
        }

        @Test
        @DisplayName("Should return string starting with class name")
        void shouldReturnStringStartingWithClassName() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            String result = tournament.toString();

            // Then
            assertTrue(result.startsWith("Tournament{"));
            assertTrue(result.endsWith("}"));
        }

        @Test
        @DisplayName("Should return consistent string on multiple calls")
        void shouldReturnConsistentStringOnMultipleCalls() {
            // Given
            Tournament tournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            // When
            String result1 = tournament.toString();
            String result2 = tournament.toString();

            // Then
            assertEquals(result1, result2);
        }
    }
}
