package com.personal.tournament_api.team.domain;

import com.personal.tournament_api.team.domain.exceptions.DuplicateTeamNameException;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamDomainService Unit Tests")
class TeamDomainServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamDomainService teamDomainService;

    @Nested
    @DisplayName("Validate Unique Team Name Tests")
    class ValidateUniqueTeamNameTests {

        @Test
        @DisplayName("Should pass validation when team name does not exist")
        void shouldPassValidationWhenTeamNameDoesNotExist() {
            // Given
            String teamName = "Real Madrid";
            when(teamRepository.existsByName(teamName)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueTeamName(teamName, teamRepository);
            });

            verify(teamRepository, times(1)).existsByName(teamName);
        }

        @Test
        @DisplayName("Should throw DuplicateTeamNameException when team name already exists")
        void shouldThrowExceptionWhenTeamNameAlreadyExists() {
            // Given
            String teamName = "Real Madrid";
            when(teamRepository.existsByName(teamName)).thenReturn(true);

            // When & Then
            DuplicateTeamNameException exception = assertThrows(
                DuplicateTeamNameException.class,
                () -> teamDomainService.validateUniqueTeamName(teamName, teamRepository)
            );

            assertNotNull(exception);
            assertTrue(exception.getMessage().contains(teamName));

            verify(teamRepository, times(1)).existsByName(teamName);
        }

        @Test
        @DisplayName("Should validate with different team names")
        void shouldValidateWithDifferentTeamNames() {
            // Given
            String teamName1 = "Barcelona";
            String teamName2 = "Sevilla";

            when(teamRepository.existsByName(teamName1)).thenReturn(false);
            when(teamRepository.existsByName(teamName2)).thenReturn(true);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueTeamName(teamName1, teamRepository);
            });

            assertThrows(DuplicateTeamNameException.class, () -> {
                teamDomainService.validateUniqueTeamName(teamName2, teamRepository);
            });

            verify(teamRepository, times(1)).existsByName(teamName1);
            verify(teamRepository, times(1)).existsByName(teamName2);
        }

        @Test
        @DisplayName("Should delegate existence check to repository")
        void shouldDelegateExistenceCheckToRepository() {
            // Given
            String teamName = "Atletico Madrid";
            when(teamRepository.existsByName(teamName)).thenReturn(false);

            // When
            teamDomainService.validateUniqueTeamName(teamName, teamRepository);

            // Then
            verify(teamRepository, times(1)).existsByName(teamName);
        }

        @Test
        @DisplayName("Should throw exception with correct team name in message")
        void shouldThrowExceptionWithCorrectTeamNameInMessage() {
            // Given
            String teamName = "Valencia CF";
            when(teamRepository.existsByName(teamName)).thenReturn(true);

            // When & Then
            DuplicateTeamNameException exception = assertThrows(
                DuplicateTeamNameException.class,
                () -> teamDomainService.validateUniqueTeamName(teamName, teamRepository)
            );

            assertTrue(exception.getMessage().contains("Valencia CF"));
        }

        @Test
        @DisplayName("Should validate case sensitive team names")
        void shouldValidateCaseSensitiveTeamNames() {
            // Given
            String teamName = "REAL MADRID";
            when(teamRepository.existsByName(teamName)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueTeamName(teamName, teamRepository);
            });

            verify(teamRepository, times(1)).existsByName(teamName);
        }
    }

    @Nested
    @DisplayName("Validate Unique Name For Update Tests")
    class ValidateUniqueNameForUpdateTests {

        @Test
        @DisplayName("Should pass validation when name is unique for other teams")
        void shouldPassValidationWhenNameIsUniqueForOtherTeams() {
            // Given
            String teamName = "Real Madrid CF";
            Long teamId = 1L;
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository);
            });

            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
        }

        @Test
        @DisplayName("Should throw DuplicateTeamNameException when name exists for another team")
        void shouldThrowExceptionWhenNameExistsForAnotherTeam() {
            // Given
            String teamName = "Barcelona";
            Long teamId = 1L;
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(true);

            // When & Then
            DuplicateTeamNameException exception = assertThrows(
                DuplicateTeamNameException.class,
                () -> teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository)
            );

            assertNotNull(exception);
            assertTrue(exception.getMessage().contains(teamName));

            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
        }

        @Test
        @DisplayName("Should allow same team to keep its own name")
        void shouldAllowSameTeamToKeepItsOwnName() {
            // Given - Team with id=1 is updating to its current name "Real Madrid"
            String teamName = "Real Madrid";
            Long teamId = 1L;
            // Repository should return false because no OTHER team has this name
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository);
            });

            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
        }

        @Test
        @DisplayName("Should prevent team from using another team's name")
        void shouldPreventTeamFromUsingAnotherTeamsName() {
            // Given - Team with id=1 is trying to update to "Barcelona" which is used by team id=2
            String teamName = "Barcelona";
            Long teamId = 1L;
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(true);

            // When & Then
            assertThrows(DuplicateTeamNameException.class, () -> {
                teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository);
            });

            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
        }

        @Test
        @DisplayName("Should validate with different team ids")
        void shouldValidateWithDifferentTeamIds() {
            // Given
            String teamName = "Sevilla";
            Long teamId1 = 1L;
            Long teamId2 = 2L;

            when(teamRepository.existsByNameAndIdNot(teamName, teamId1)).thenReturn(false);
            when(teamRepository.existsByNameAndIdNot(teamName, teamId2)).thenReturn(true);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueNameForUpdate(teamName, teamId1, teamRepository);
            });

            assertThrows(DuplicateTeamNameException.class, () -> {
                teamDomainService.validateUniqueNameForUpdate(teamName, teamId2, teamRepository);
            });

            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId1);
            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId2);
        }

        @Test
        @DisplayName("Should delegate uniqueness check to repository")
        void shouldDelegateUniquenessCheckToRepository() {
            // Given
            String teamName = "Athletic Bilbao";
            Long teamId = 5L;
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(false);

            // When
            teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository);

            // Then
            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
        }

        @Test
        @DisplayName("Should throw exception with correct team name in message for update")
        void shouldThrowExceptionWithCorrectTeamNameInMessageForUpdate() {
            // Given
            String teamName = "Real Sociedad";
            Long teamId = 3L;
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(true);

            // When & Then
            DuplicateTeamNameException exception = assertThrows(
                DuplicateTeamNameException.class,
                () -> teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository)
            );

            assertTrue(exception.getMessage().contains("Real Sociedad"));
        }

        @Test
        @DisplayName("Should handle updates with same name but different case")
        void shouldHandleUpdatesWithSameNameButDifferentCase() {
            // Given
            String teamName = "SEVILLA FC";
            Long teamId = 7L;
            when(teamRepository.existsByNameAndIdNot(teamName, teamId)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueNameForUpdate(teamName, teamId, teamRepository);
            });

            verify(teamRepository, times(1)).existsByNameAndIdNot(teamName, teamId);
        }
    }

    @Nested
    @DisplayName("Business Logic Integration Tests")
    class BusinessLogicIntegrationTests {

        @Test
        @DisplayName("Should handle creation and update validation scenarios")
        void shouldHandleCreationAndUpdateValidationScenarios() {
            // Given - Scenario: Creating "Real Madrid" then trying to create another "Real Madrid"
            String teamName = "Real Madrid";

            // First creation - name doesn't exist
            when(teamRepository.existsByName(teamName)).thenReturn(false);
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueTeamName(teamName, teamRepository);
            });

            // Second creation - name now exists
            when(teamRepository.existsByName(teamName)).thenReturn(true);
            assertThrows(DuplicateTeamNameException.class, () -> {
                teamDomainService.validateUniqueTeamName(teamName, teamRepository);
            });

            verify(teamRepository, times(2)).existsByName(teamName);
        }

        @Test
        @DisplayName("Should properly distinguish between create and update validations")
        void shouldProperlyDistinguishBetweenCreateAndUpdateValidations() {
            // Given
            String existingTeamName = "Barcelona";
            Long existingTeamId = 1L;

            // Scenario 1: Creating a new team with existing name should fail
            when(teamRepository.existsByName(existingTeamName)).thenReturn(true);
            assertThrows(DuplicateTeamNameException.class, () -> {
                teamDomainService.validateUniqueTeamName(existingTeamName, teamRepository);
            });

            // Scenario 2: Updating existing team with its own name should pass
            when(teamRepository.existsByNameAndIdNot(existingTeamName, existingTeamId)).thenReturn(false);
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueNameForUpdate(existingTeamName, existingTeamId, teamRepository);
            });

            verify(teamRepository, times(1)).existsByName(existingTeamName);
            verify(teamRepository, times(1)).existsByNameAndIdNot(existingTeamName, existingTeamId);
        }

        @Test
        @DisplayName("Should validate multiple teams with unique names")
        void shouldValidateMultipleTeamsWithUniqueNames() {
            // Given
            String team1 = "Real Madrid";
            String team2 = "Barcelona";
            String team3 = "Atletico Madrid";

            when(teamRepository.existsByName(team1)).thenReturn(false);
            when(teamRepository.existsByName(team2)).thenReturn(false);
            when(teamRepository.existsByName(team3)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> {
                teamDomainService.validateUniqueTeamName(team1, teamRepository);
                teamDomainService.validateUniqueTeamName(team2, teamRepository);
                teamDomainService.validateUniqueTeamName(team3, teamRepository);
            });

            verify(teamRepository, times(1)).existsByName(team1);
            verify(teamRepository, times(1)).existsByName(team2);
            verify(teamRepository, times(1)).existsByName(team3);
        }
    }
}
