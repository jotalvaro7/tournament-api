package com.personal.tournament_api.team.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Team Domain Exceptions Unit Tests")
class TeamDomainExceptionsTest {

    @Nested
    @DisplayName("DuplicateTeamNameException Tests")
    class DuplicateTeamNameExceptionTests {

        @Test
        @DisplayName("Should create exception with team name in message")
        void shouldCreateExceptionWithTeamNameInMessage() {
            // Given
            String teamName = "Real Madrid";

            // When
            DuplicateTeamNameException exception = new DuplicateTeamNameException(teamName);

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains(teamName));
            assertTrue(exception.getMessage().contains("already exists"));
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            DuplicateTeamNameException exception = new DuplicateTeamNameException("Barcelona");

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should create exception with different team names")
        void shouldCreateExceptionWithDifferentTeamNames() {
            // Given
            String name1 = "Barcelona";
            String name2 = "Sevilla";

            // When
            DuplicateTeamNameException exception1 = new DuplicateTeamNameException(name1);
            DuplicateTeamNameException exception2 = new DuplicateTeamNameException(name2);

            // Then
            assertTrue(exception1.getMessage().contains(name1));
            assertTrue(exception2.getMessage().contains(name2));
            assertNotEquals(exception1.getMessage(), exception2.getMessage());
        }

        @Test
        @DisplayName("Should have RULE_VIOLATION error type")
        void shouldHaveRuleViolationErrorType() {
            // Given & When
            DuplicateTeamNameException exception = new DuplicateTeamNameException("Real Madrid");

            // Then
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("InvalidTeamNameException Tests")
    class InvalidTeamNameExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidTeamNameException exception = new InvalidTeamNameException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("invalid"));
            assertTrue(exception.getMessage().contains("3 and 100 characters"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            InvalidTeamNameException exception = new InvalidTeamNameException();

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorType() {
            // Given & When
            InvalidTeamNameException exception = new InvalidTeamNameException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should have consistent message across instances")
        void shouldHaveConsistentMessageAcrossInstances() {
            // Given & When
            InvalidTeamNameException exception1 = new InvalidTeamNameException();
            InvalidTeamNameException exception2 = new InvalidTeamNameException();

            // Then
            assertEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("InvalidTeamCoachException Tests")
    class InvalidTeamCoachExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidTeamCoachException exception = new InvalidTeamCoachException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("invalid"));
            assertTrue(exception.getMessage().contains("coach"));
            assertTrue(exception.getMessage().contains("3 and 100 characters"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            InvalidTeamCoachException exception = new InvalidTeamCoachException();

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorType() {
            // Given & When
            InvalidTeamCoachException exception = new InvalidTeamCoachException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should have consistent message across instances")
        void shouldHaveConsistentMessageAcrossInstances() {
            // Given & When
            InvalidTeamCoachException exception1 = new InvalidTeamCoachException();
            InvalidTeamCoachException exception2 = new InvalidTeamCoachException();

            // Then
            assertEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("InvalidTeamGoalsException Tests")
    class InvalidTeamGoalsExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidTeamGoalsException exception = new InvalidTeamGoalsException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("Invalid"));
            assertTrue(exception.getMessage().contains("goals"));
            assertTrue(exception.getMessage().contains("non-negative"));
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            InvalidTeamGoalsException exception = new InvalidTeamGoalsException();

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have VALIDATION_ERROR error type")
        void shouldHaveValidationErrorType() {
            // Given & When
            InvalidTeamGoalsException exception = new InvalidTeamGoalsException();

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception.getErrorType());
        }

        @Test
        @DisplayName("Should have consistent message across instances")
        void shouldHaveConsistentMessageAcrossInstances() {
            // Given & When
            InvalidTeamGoalsException exception1 = new InvalidTeamGoalsException();
            InvalidTeamGoalsException exception2 = new InvalidTeamGoalsException();

            // Then
            assertEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("InvalidTeamTournamentIdException Tests")
    class InvalidTeamTournamentIdExceptionTests {

        @Test
        @DisplayName("Should create exception with validation message")
        void shouldCreateExceptionWithValidationMessage() {
            // When
            InvalidTeamTournamentIdException exception = new InvalidTeamTournamentIdException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("Invalid"));
            assertTrue(exception.getMessage().contains("tournament ID"));
            assertTrue(exception.getMessage().contains("positive number"));
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            InvalidTeamTournamentIdException exception = new InvalidTeamTournamentIdException();

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have RULE_VIOLATION error type")
        void shouldHaveRuleViolationErrorType() {
            // Given & When
            InvalidTeamTournamentIdException exception = new InvalidTeamTournamentIdException();

            // Then
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should have consistent message across instances")
        void shouldHaveConsistentMessageAcrossInstances() {
            // Given & When
            InvalidTeamTournamentIdException exception1 = new InvalidTeamTournamentIdException();
            InvalidTeamTournamentIdException exception2 = new InvalidTeamTournamentIdException();

            // Then
            assertEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("TeamNotFoundException Tests")
    class TeamNotFoundExceptionTests {

        @Test
        @DisplayName("Should create exception with team id in message")
        void shouldCreateExceptionWithTeamIdInMessage() {
            // Given
            Long teamId = 123L;

            // When
            TeamNotFoundException exception = new TeamNotFoundException(teamId);

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("123"));
            assertTrue(exception.getMessage().contains("not found"));
            assertEquals(DomainErrorType.NOT_FOUND, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            TeamNotFoundException exception = new TeamNotFoundException(1L);

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should create exception with different team ids")
        void shouldCreateExceptionWithDifferentTeamIds() {
            // Given
            Long id1 = 1L;
            Long id2 = 999L;

            // When
            TeamNotFoundException exception1 = new TeamNotFoundException(id1);
            TeamNotFoundException exception2 = new TeamNotFoundException(id2);

            // Then
            assertTrue(exception1.getMessage().contains("1"));
            assertTrue(exception2.getMessage().contains("999"));
            assertNotEquals(exception1.getMessage(), exception2.getMessage());
        }

        @Test
        @DisplayName("Should have NOT_FOUND error type")
        void shouldHaveNotFoundErrorType() {
            // Given & When
            TeamNotFoundException exception = new TeamNotFoundException(1L);

            // Then
            assertEquals(DomainErrorType.NOT_FOUND, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("TeamGoalsDifferenceException Tests")
    class TeamGoalsDifferenceExceptionTests {

        @Test
        @DisplayName("Should create exception with consistency message")
        void shouldCreateExceptionWithConsistencyMessage() {
            // When
            TeamGoalsDifferenceException exception = new TeamGoalsDifferenceException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("Inconsistent"));
            assertTrue(exception.getMessage().contains("goals difference"));
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            TeamGoalsDifferenceException exception = new TeamGoalsDifferenceException();

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have RULE_VIOLATION error type")
        void shouldHaveRuleViolationErrorType() {
            // Given & When
            TeamGoalsDifferenceException exception = new TeamGoalsDifferenceException();

            // Then
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should have consistent message across instances")
        void shouldHaveConsistentMessageAcrossInstances() {
            // Given & When
            TeamGoalsDifferenceException exception1 = new TeamGoalsDifferenceException();
            TeamGoalsDifferenceException exception2 = new TeamGoalsDifferenceException();

            // Then
            assertEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("TeamMatchesPlayedException Tests")
    class TeamMatchesPlayedExceptionTests {

        @Test
        @DisplayName("Should create exception with consistency message")
        void shouldCreateExceptionWithConsistencyMessage() {
            // When
            TeamMatchesPlayedException exception = new TeamMatchesPlayedException();

            // Then
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("Inconsistent"));
            assertTrue(exception.getMessage().contains("matches played"));
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should be instance of TeamDomainException")
        void shouldBeInstanceOfTeamDomainException() {
            // Given
            TeamMatchesPlayedException exception = new TeamMatchesPlayedException();

            // Then
            assertInstanceOf(TeamDomainException.class, exception);
        }

        @Test
        @DisplayName("Should have RULE_VIOLATION error type")
        void shouldHaveRuleViolationErrorType() {
            // Given & When
            TeamMatchesPlayedException exception = new TeamMatchesPlayedException();

            // Then
            assertEquals(DomainErrorType.RULE_VIOLATION, exception.getErrorType());
        }

        @Test
        @DisplayName("Should have consistent message across instances")
        void shouldHaveConsistentMessageAcrossInstances() {
            // Given & When
            TeamMatchesPlayedException exception1 = new TeamMatchesPlayedException();
            TeamMatchesPlayedException exception2 = new TeamMatchesPlayedException();

            // Then
            assertEquals(exception1.getMessage(), exception2.getMessage());
        }
    }

    @Nested
    @DisplayName("TeamDomainException Tests")
    class TeamDomainExceptionTests {

        @Test
        @DisplayName("Should create exception with message and error type")
        void shouldCreateExceptionWithMessageAndErrorType() {
            // Given
            String message = "Test error message";
            DomainErrorType errorType = DomainErrorType.VALIDATION_ERROR;

            // When
            TeamDomainException exception = new TeamDomainException(message, errorType);

            // Then
            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(errorType, exception.getErrorType());
        }

        @Test
        @DisplayName("Should create exception with different error types")
        void shouldCreateExceptionWithDifferentErrorTypes() {
            // Given
            String message = "Test message";

            // When
            TeamDomainException exception1 = new TeamDomainException(message, DomainErrorType.VALIDATION_ERROR);
            TeamDomainException exception2 = new TeamDomainException(message, DomainErrorType.RULE_VIOLATION);
            TeamDomainException exception3 = new TeamDomainException(message, DomainErrorType.NOT_FOUND);

            // Then
            assertEquals(DomainErrorType.VALIDATION_ERROR, exception1.getErrorType());
            assertEquals(DomainErrorType.RULE_VIOLATION, exception2.getErrorType());
            assertEquals(DomainErrorType.NOT_FOUND, exception3.getErrorType());
        }

        @Test
        @DisplayName("Should be a RuntimeException")
        void shouldBeRuntimeException() {
            // Given
            TeamDomainException exception = new TeamDomainException("Test", DomainErrorType.VALIDATION_ERROR);

            // Then
            assertInstanceOf(RuntimeException.class, exception);
        }
    }

    @Nested
    @DisplayName("Exception Hierarchy Tests")
    class ExceptionHierarchyTests {

        @Test
        @DisplayName("All team exceptions should extend TeamDomainException")
        void allTeamExceptionsShouldExtendTeamDomainException() {
            // Given & When & Then
            assertInstanceOf(TeamDomainException.class, new DuplicateTeamNameException("test"));
            assertInstanceOf(TeamDomainException.class, new InvalidTeamNameException());
            assertInstanceOf(TeamDomainException.class, new InvalidTeamCoachException());
            assertInstanceOf(TeamDomainException.class, new InvalidTeamGoalsException());
            assertInstanceOf(TeamDomainException.class, new InvalidTeamTournamentIdException());
            assertInstanceOf(TeamDomainException.class, new TeamNotFoundException(1L));
            assertInstanceOf(TeamDomainException.class, new TeamGoalsDifferenceException());
            assertInstanceOf(TeamDomainException.class, new TeamMatchesPlayedException());
        }

        @Test
        @DisplayName("All team exceptions should be RuntimeExceptions")
        void allTeamExceptionsShouldBeRuntimeExceptions() {
            // Given & When & Then
            assertInstanceOf(RuntimeException.class, new DuplicateTeamNameException("test"));
            assertInstanceOf(RuntimeException.class, new InvalidTeamNameException());
            assertInstanceOf(RuntimeException.class, new InvalidTeamCoachException());
            assertInstanceOf(RuntimeException.class, new InvalidTeamGoalsException());
            assertInstanceOf(RuntimeException.class, new InvalidTeamTournamentIdException());
            assertInstanceOf(RuntimeException.class, new TeamNotFoundException(1L));
            assertInstanceOf(RuntimeException.class, new TeamGoalsDifferenceException());
            assertInstanceOf(RuntimeException.class, new TeamMatchesPlayedException());
        }

        @Test
        @DisplayName("Should verify error type classification")
        void shouldVerifyErrorTypeClassification() {
            // Validation errors
            assertEquals(DomainErrorType.VALIDATION_ERROR, new InvalidTeamNameException().getErrorType());
            assertEquals(DomainErrorType.VALIDATION_ERROR, new InvalidTeamCoachException().getErrorType());
            assertEquals(DomainErrorType.VALIDATION_ERROR, new InvalidTeamGoalsException().getErrorType());

            // Rule violations
            assertEquals(DomainErrorType.RULE_VIOLATION, new DuplicateTeamNameException("test").getErrorType());
            assertEquals(DomainErrorType.RULE_VIOLATION, new InvalidTeamTournamentIdException().getErrorType());
            assertEquals(DomainErrorType.RULE_VIOLATION, new TeamGoalsDifferenceException().getErrorType());
            assertEquals(DomainErrorType.RULE_VIOLATION, new TeamMatchesPlayedException().getErrorType());

            // Not found
            assertEquals(DomainErrorType.NOT_FOUND, new TeamNotFoundException(1L).getErrorType());
        }
    }
}
