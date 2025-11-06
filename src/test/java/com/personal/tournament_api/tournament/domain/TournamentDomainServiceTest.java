package com.personal.tournament_api.tournament.domain;

import com.personal.tournament_api.tournament.domain.exceptions.DuplicateTournamentNameException;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Tournament Domain Service Unit Tests")
@ExtendWith(MockitoExtension.class)
class TournamentDomainServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    private TournamentDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new TournamentDomainService();
    }

    @Nested
    @DisplayName("Validate Unique Name Tests")
    class ValidateUniqueNameTests {

        @Test
        @DisplayName("Should validate successfully when name does not exist")
        void shouldValidateSuccessfullyWhenNameDoesNotExist() {
            // Given
            String name = "La Liga";
            when(tournamentRepository.existsByName(name)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> domainService.validateUniqueName(name, tournamentRepository));
            verify(tournamentRepository, times(1)).existsByName(name);
        }

        @Test
        @DisplayName("Should throw exception when name already exists")
        void shouldThrowExceptionWhenNameAlreadyExists() {
            // Given
            String name = "La Liga";
            when(tournamentRepository.existsByName(name)).thenReturn(true);

            // When & Then
            DuplicateTournamentNameException exception = assertThrows(
                    DuplicateTournamentNameException.class,
                    () -> domainService.validateUniqueName(name, tournamentRepository)
            );

            assertNotNull(exception);
            verify(tournamentRepository, times(1)).existsByName(name);
        }

        @Test
        @DisplayName("Should call repository with correct name parameter")
        void shouldCallRepositoryWithCorrectNameParameter() {
            // Given
            String name = "Premier League";
            when(tournamentRepository.existsByName(name)).thenReturn(false);

            // When
            domainService.validateUniqueName(name, tournamentRepository);

            // Then
            verify(tournamentRepository).existsByName(name);
            verifyNoMoreInteractions(tournamentRepository);
        }
    }

    @Nested
    @DisplayName("Validate Unique Name For Update Tests")
    class ValidateUniqueNameForUpdateTests {

        @Test
        @DisplayName("Should validate successfully when name does not exist for other tournaments")
        void shouldValidateSuccessfullyWhenNameDoesNotExistForOtherTournaments() {
            // Given
            String name = "La Liga";
            Long tournamentId = 1L;
            when(tournamentRepository.existsByNameAndIdNot(name, tournamentId)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> domainService.validateUniqueNameForUpdate(name, tournamentId, tournamentRepository));
            verify(tournamentRepository, times(1)).existsByNameAndIdNot(name, tournamentId);
        }

        @Test
        @DisplayName("Should throw exception when name exists for another tournament")
        void shouldThrowExceptionWhenNameExistsForAnotherTournament() {
            // Given
            String name = "La Liga";
            Long tournamentId = 1L;
            when(tournamentRepository.existsByNameAndIdNot(name, tournamentId)).thenReturn(true);

            // When & Then
            DuplicateTournamentNameException exception = assertThrows(
                    DuplicateTournamentNameException.class,
                    () -> domainService.validateUniqueNameForUpdate(name, tournamentId, tournamentRepository)
            );

            assertNotNull(exception);
            verify(tournamentRepository, times(1)).existsByNameAndIdNot(name, tournamentId);
        }

        @Test
        @DisplayName("Should allow same tournament to keep its name")
        void shouldAllowSameTournamentToKeepItsName() {
            // Given
            String name = "La Liga";
            Long tournamentId = 1L;
            when(tournamentRepository.existsByNameAndIdNot(name, tournamentId)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> domainService.validateUniqueNameForUpdate(name, tournamentId, tournamentRepository));
            verify(tournamentRepository, times(1)).existsByNameAndIdNot(name, tournamentId);
        }

        @Test
        @DisplayName("Should call repository with correct parameters")
        void shouldCallRepositoryWithCorrectParameters() {
            // Given
            String name = "Premier League";
            Long tournamentId = 5L;
            when(tournamentRepository.existsByNameAndIdNot(name, tournamentId)).thenReturn(false);

            // When
            domainService.validateUniqueNameForUpdate(name, tournamentId, tournamentRepository);

            // Then
            verify(tournamentRepository).existsByNameAndIdNot(name, tournamentId);
            verifyNoMoreInteractions(tournamentRepository);
        }

        @Test
        @DisplayName("Should validate different names for different tournament IDs")
        void shouldValidateDifferentNamesForDifferentTournamentIds() {
            // Given
            String name1 = "La Liga";
            String name2 = "Premier League";
            Long tournamentId1 = 1L;
            Long tournamentId2 = 2L;

            when(tournamentRepository.existsByNameAndIdNot(name1, tournamentId1)).thenReturn(false);
            when(tournamentRepository.existsByNameAndIdNot(name2, tournamentId2)).thenReturn(false);

            // When & Then
            assertDoesNotThrow(() -> domainService.validateUniqueNameForUpdate(name1, tournamentId1, tournamentRepository));
            assertDoesNotThrow(() -> domainService.validateUniqueNameForUpdate(name2, tournamentId2, tournamentRepository));

            verify(tournamentRepository).existsByNameAndIdNot(name1, tournamentId1);
            verify(tournamentRepository).existsByNameAndIdNot(name2, tournamentId2);
        }
    }
}
