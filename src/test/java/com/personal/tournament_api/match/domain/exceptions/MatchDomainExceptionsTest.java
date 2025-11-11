package com.personal.tournament_api.match.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Match Domain Exceptions Unit Tests")
class MatchDomainExceptionsTest {

    @Nested
    @DisplayName("MatchDomainException Tests")
    class MatchDomainExceptionTests {

        @Test
        @DisplayName("Should create exception with message and error type")
        void shouldCreateExceptionWithMessageAndErrorType() {
            // When
            MatchDomainException exception = new MatchDomainException(
                "Test message",
                DomainErrorType.VALIDATION_ERROR
            );

            // Then
            assertNotNull(exception);
            assertEquals("Test message", exception.getMessage());
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of DomainException")
        void shouldBeInstanceOfDomainException() {
            // When
            MatchDomainException exception = new MatchDomainException(
                "Test",
                DomainErrorType.VALIDATION_ERROR
            );

            // Then
            assertInstanceOf(RuntimeException.class, exception);
        }
    }

    @Nested
    @DisplayName("InvalidMatchTournamentIdException Tests")
    class InvalidMatchTournamentIdExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidMatchTournamentIdException exception = new InvalidMatchTournamentIdException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("tournament ID"));
            assertTrue(exception.getMessage().contains("positive"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchTournamentIdException exception = new InvalidMatchTournamentIdException();

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorErrorType() {
            // When
            InvalidMatchTournamentIdException exception = new InvalidMatchTournamentIdException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("InvalidMatchTeamsException Tests")
    class InvalidMatchTeamsExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidMatchTeamsException exception = new InvalidMatchTeamsException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("teams"));
            assertTrue(exception.getMessage().contains("invalid"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchTeamsException exception = new InvalidMatchTeamsException();

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorErrorType() {
            // When
            InvalidMatchTeamsException exception = new InvalidMatchTeamsException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("InvalidMatchFieldException Tests")
    class InvalidMatchFieldExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidMatchFieldException exception = new InvalidMatchFieldException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("field"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchFieldException exception = new InvalidMatchFieldException();

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorErrorType() {
            // When
            InvalidMatchFieldException exception = new InvalidMatchFieldException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("InvalidMatchDateException Tests")
    class InvalidMatchDateExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidMatchDateException exception = new InvalidMatchDateException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("date"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchDateException exception = new InvalidMatchDateException();

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorErrorType() {
            // When
            InvalidMatchDateException exception = new InvalidMatchDateException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("InvalidMatchScoreException Tests")
    class InvalidMatchScoreExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidMatchScoreException exception = new InvalidMatchScoreException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("score"));
            assertTrue(exception.getMessage().contains("negative"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchScoreException exception = new InvalidMatchScoreException();

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorErrorType() {
            // When
            InvalidMatchScoreException exception = new InvalidMatchScoreException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("InvalidMatchStatusTransitionException Tests")
    class InvalidMatchStatusTransitionExceptionTests {

        @Test
        @DisplayName("Should create exception with custom message")
        void shouldCreateExceptionWithCustomMessage() {
            // Given
            String customMessage = "Cannot update a finished match";

            // When
            InvalidMatchStatusTransitionException exception =
                new InvalidMatchStatusTransitionException(customMessage);

            // Then
            assertNotNull(exception);
            assertEquals(customMessage, exception.getMessage());
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchStatusTransitionException exception =
                new InvalidMatchStatusTransitionException("Test message");

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have RULE_VIOLATION error type")
        void shouldHaveRuleViolationErrorType() {
            // When
            InvalidMatchStatusTransitionException exception =
                new InvalidMatchStatusTransitionException("Test message");

            // Then
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should create exception with different messages")
        void shouldCreateExceptionWithDifferentMessages() {
            // Given
            String message1 = "Cannot postpone a finished match";
            String message2 = "Cannot set result for a postponed match";

            // When
            InvalidMatchStatusTransitionException exception1 =
                new InvalidMatchStatusTransitionException(message1);
            InvalidMatchStatusTransitionException exception2 =
                new InvalidMatchStatusTransitionException(message2);

            // Then
            assertEquals(message1, exception1.getMessage());
            assertEquals(message2, exception2.getMessage());
            assertNotEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("MatchNotFoundException Tests")
    class MatchNotFoundExceptionTests {

        @Test
        @DisplayName("Should create exception with match ID in message")
        void shouldCreateExceptionWithMatchIdInMessage() {
            // Given
            Long matchId = 123L;

            // When
            MatchNotFoundException exception = new MatchNotFoundException(matchId);

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains(matchId.toString()));
            assertTrue(exception.getMessage().contains("not found"));
            assertEquals(DomainErrorType.NOT_FOUND, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            MatchNotFoundException exception = new MatchNotFoundException(1L);

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have NOT_FOUND error type")
        void shouldHaveNotFoundErrorType() {
            // When
            MatchNotFoundException exception = new MatchNotFoundException(1L);

            // Then
            assertEquals(DomainErrorType.NOT_FOUND, exception.getErrorType());
        }

        @Test
        @DisplayName("Should create exception with different match IDs")
        void shouldCreateExceptionWithDifferentMatchIds() {
            // Given
            Long id1 = 100L;
            Long id2 = 200L;

            // When
            MatchNotFoundException exception1 = new MatchNotFoundException(id1);
            MatchNotFoundException exception2 = new MatchNotFoundException(id2);

            // Then
            assertTrue(exception1.getMessage().contains(id1.toString()));
            assertTrue(exception2.getMessage().contains(id2.toString()));
            assertNotEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("InvalidMatchDataException Tests")
    class InvalidMatchDataExceptionTests {

        @Test
        @DisplayName("Should create exception with custom message")
        void shouldCreateExceptionWithCustomMessage() {
            // Given
            String customMessage = "Home team does not belong to tournament";

            // When
            InvalidMatchDataException exception = new InvalidMatchDataException(customMessage);

            // Then
            assertNotNull(exception);
            assertEquals(customMessage, exception.getMessage());
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of MatchDomainException")
        void shouldBeInstanceOfMatchDomainException() {
            // When
            InvalidMatchDataException exception = new InvalidMatchDataException("Test message");

            // Then
            assertInstanceOf(MatchDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorErrorType() {
            // When
            InvalidMatchDataException exception = new InvalidMatchDataException("Test message");

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should create exception with different messages")
        void shouldCreateExceptionWithDifferentMessages() {
            // Given
            String message1 = "Home team 1 does not belong to tournament 10";
            String message2 = "Away team 2 does not belong to tournament 10";

            // When
            InvalidMatchDataException exception1 = new InvalidMatchDataException(message1);
            InvalidMatchDataException exception2 = new InvalidMatchDataException(message2);

            // Then
            assertEquals(message1, exception1.getMessage());
            assertEquals(message2, exception2.getMessage());
            assertNotEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("Exception Hierarchy Tests")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("All match exceptions should extend MatchDomainException")
        void allMatchExceptionsShouldExtendMatchDomainException() {
            assertInstanceOf(MatchDomainException.class, new InvalidMatchTournamentIdException());
            assertInstanceOf(MatchDomainException.class, new InvalidMatchTeamsException());
            assertInstanceOf(MatchDomainException.class, new InvalidMatchFieldException());
            assertInstanceOf(MatchDomainException.class, new InvalidMatchDateException());
            assertInstanceOf(MatchDomainException.class, new InvalidMatchScoreException());
            assertInstanceOf(MatchDomainException.class, new InvalidMatchStatusTransitionException("test"));
            assertInstanceOf(MatchDomainException.class, new MatchNotFoundException(1L));
            assertInstanceOf(MatchDomainException.class, new InvalidMatchDataException("test"));
        }

        @Test
        @DisplayName("All match exceptions should be RuntimeExceptions")
        void allMatchExceptionsShouldBeRuntimeExceptions() {
            assertInstanceOf(RuntimeException.class, new InvalidMatchTournamentIdException());
            assertInstanceOf(RuntimeException.class, new InvalidMatchTeamsException());
            assertInstanceOf(RuntimeException.class, new InvalidMatchFieldException());
            assertInstanceOf(RuntimeException.class, new InvalidMatchDateException());
            assertInstanceOf(RuntimeException.class, new InvalidMatchScoreException());
            assertInstanceOf(RuntimeException.class, new InvalidMatchStatusTransitionException("test"));
            assertInstanceOf(RuntimeException.class, new MatchNotFoundException(1L));
            assertInstanceOf(RuntimeException.class, new InvalidMatchDataException("test"));
        }
    }
}
