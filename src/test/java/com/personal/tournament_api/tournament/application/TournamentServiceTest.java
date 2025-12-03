package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import com.personal.tournament_api.tournament.application.usecases.CreateTournamentUseCase.CreateTournamentCommand;
import com.personal.tournament_api.tournament.application.usecases.UpdateTournamentUseCase.UpdateTournamentCommand;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.*;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Tournament Service Unit Tests")
@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentDomainService tournamentDomainService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @Nested
    @DisplayName("Create Tournament Tests")
    class CreateTournamentTests {

        @Test
        @DisplayName("Should create tournament successfully with valid data")
        void shouldCreateTournamentSuccessfully() {
            // Given
            CreateTournamentCommand command = new CreateTournamentCommand("La Liga", "Spanish Football Championship");
            Tournament savedTournament = new Tournament(1L, "La Liga", "Spanish Football Championship");

            doNothing().when(tournamentDomainService).validateUniqueName(anyString(), any(TournamentRepository.class));
            when(tournamentRepository.save(any(Tournament.class))).thenReturn(savedTournament);

            // When
            Tournament result = tournamentService.create(command);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("La Liga", result.getName());
            assertEquals("Spanish Football Championship", result.getDescription());
            assertEquals(StatusTournament.CREATED, result.getStatus());

            verify(tournamentDomainService).validateUniqueName("La Liga", tournamentRepository);
            verify(tournamentRepository).save(any(Tournament.class));
        }

        @Test
        @DisplayName("Should throw exception when name already exists")
        void shouldThrowExceptionWhenNameAlreadyExists() {
            // Given
            CreateTournamentCommand command = new CreateTournamentCommand("La Liga", "Spanish Football Championship");

            doThrow(new DuplicateTournamentNameException("La Liga"))
                    .when(tournamentDomainService)
                    .validateUniqueName(anyString(), any(TournamentRepository.class));

            // When & Then
            assertThrows(DuplicateTournamentNameException.class, () -> {
                tournamentService.create(command);
            });

            verify(tournamentDomainService).validateUniqueName("La Liga", tournamentRepository);
            verify(tournamentRepository, never()).save(any(Tournament.class));
        }

        @Test
        @DisplayName("Should create tournament with CREATED status by default")
        void shouldCreateTournamentWithCreatedStatus() {
            // Given
            CreateTournamentCommand command = new CreateTournamentCommand("Premier League", "English Football Championship");
            Tournament savedTournament = new Tournament(2L, "Premier League", "English Football Championship");

            doNothing().when(tournamentDomainService).validateUniqueName(anyString(), any(TournamentRepository.class));
            when(tournamentRepository.save(any(Tournament.class))).thenReturn(savedTournament);

            // When
            Tournament result = tournamentService.create(command);

            // Then
            assertEquals(StatusTournament.CREATED, result.getStatus());
        }

        @Test
        @DisplayName("Should save tournament with null id initially")
        void shouldSaveTournamentWithNullId() {
            // Given
            CreateTournamentCommand command = new CreateTournamentCommand("Serie A", "Italian Football Championship");
            Tournament savedTournament = new Tournament(3L, "Serie A", "Italian Football Championship");

            doNothing().when(tournamentDomainService).validateUniqueName(anyString(), any(TournamentRepository.class));
            when(tournamentRepository.save(any(Tournament.class))).thenReturn(savedTournament);

            ArgumentCaptor<Tournament> tournamentCaptor = ArgumentCaptor.forClass(Tournament.class);

            // When
            tournamentService.create(command);

            // Then
            verify(tournamentRepository).save(tournamentCaptor.capture());
            Tournament capturedTournament = tournamentCaptor.getValue();
            assertNull(capturedTournament.getId());
        }
    }

    @Nested
    @DisplayName("Update Tournament Tests")
    class UpdateTournamentTests {

        @Test
        @DisplayName("Should update tournament successfully with valid data")
        void shouldUpdateTournamentSuccessfully() {
            // Given
            Long tournamentId = 1L;
            UpdateTournamentCommand command = new UpdateTournamentCommand(tournamentId, "La Liga Santander", "Updated Description");
            Tournament existingTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            Tournament updatedTournament = new Tournament(tournamentId, "La Liga Santander", "Updated Description");

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(existingTournament));
            doNothing().when(tournamentDomainService).validateUniqueNameForUpdate(anyString(), anyLong(), any(TournamentRepository.class));
            when(tournamentRepository.save(any(Tournament.class))).thenReturn(updatedTournament);

            // When
            Tournament result = tournamentService.update(command);

            // Then
            assertNotNull(result);
            assertEquals("La Liga Santander", result.getName());
            assertEquals("Updated Description", result.getDescription());

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentDomainService).validateUniqueNameForUpdate("La Liga Santander", tournamentId, tournamentRepository);
            verify(tournamentRepository).save(existingTournament);
        }

        @Test
        @DisplayName("Should throw exception when tournament not found")
        void shouldThrowExceptionWhenTournamentNotFound() {
            // Given
            Long tournamentId = 999L;
            UpdateTournamentCommand command = new UpdateTournamentCommand(tournamentId, "New Name", "New Description");

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TournamentNotFoundException.class, () -> {
                tournamentService.update(command);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentDomainService, never()).validateUniqueNameForUpdate(anyString(), anyLong(), any(TournamentRepository.class));
            verify(tournamentRepository, never()).save(any(Tournament.class));
        }

        @Test
        @DisplayName("Should throw exception when new name already exists")
        void shouldThrowExceptionWhenNewNameAlreadyExists() {
            // Given
            Long tournamentId = 1L;
            UpdateTournamentCommand command = new UpdateTournamentCommand(tournamentId, "Premier League", "Description");
            Tournament existingTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(existingTournament));
            doThrow(new DuplicateTournamentNameException("Premier League"))
                    .when(tournamentDomainService)
                    .validateUniqueNameForUpdate(anyString(), anyLong(), any(TournamentRepository.class));

            // When & Then
            assertThrows(DuplicateTournamentNameException.class, () -> {
                tournamentService.update(command);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentDomainService).validateUniqueNameForUpdate("Premier League", tournamentId, tournamentRepository);
            verify(tournamentRepository, never()).save(any(Tournament.class));
        }
    }

    @Nested
    @DisplayName("Start Tournament Tests")
    class StartTournamentTests {

        @Test
        @DisplayName("Should start tournament successfully")
        void shouldStartTournamentSuccessfully() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            Tournament startedTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            startedTournament.startTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
            when(tournamentRepository.save(tournament)).thenReturn(startedTournament);

            // When
            Tournament result = tournamentService.start(tournamentId);

            // Then
            assertNotNull(result);
            assertEquals(StatusTournament.IN_PROGRESS, result.getStatus());

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentRepository).save(tournament);
        }

        @Test
        @DisplayName("Should throw exception when tournament not found")
        void shouldThrowExceptionWhenTournamentNotFound() {
            // Given
            Long tournamentId = 999L;
            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TournamentNotFoundException.class, () -> {
                tournamentService.start(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentRepository, never()).save(any(Tournament.class));
        }

        @Test
        @DisplayName("Should throw exception when tournament is not in CREATED status")
        void shouldThrowExceptionWhenTournamentNotInCreatedStatus() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournamentService.start(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
        }
    }

    @Nested
    @DisplayName("End Tournament Tests")
    class EndTournamentTests {

        @Test
        @DisplayName("Should end tournament successfully")
        void shouldEndTournamentSuccessfully() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            Tournament endedTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            endedTournament.startTournament();
            endedTournament.endTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
            when(tournamentRepository.save(tournament)).thenReturn(endedTournament);

            // When
            Tournament result = tournamentService.end(tournamentId);

            // Then
            assertNotNull(result);
            assertEquals(StatusTournament.COMPLETED, result.getStatus());

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentRepository).save(tournament);
        }

        @Test
        @DisplayName("Should throw exception when tournament not found")
        void shouldThrowExceptionWhenTournamentNotFound() {
            // Given
            Long tournamentId = 999L;
            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TournamentNotFoundException.class, () -> {
                tournamentService.end(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentRepository, never()).save(any(Tournament.class));
        }

        @Test
        @DisplayName("Should throw exception when tournament is not in IN_PROGRESS status")
        void shouldThrowExceptionWhenTournamentNotInProgress() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournamentService.end(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
        }
    }

    @Nested
    @DisplayName("Cancel Tournament Tests")
    class CancelTournamentTests {

        @Test
        @DisplayName("Should cancel tournament successfully")
        void shouldCancelTournamentSuccessfully() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            Tournament cancelledTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            cancelledTournament.cancelTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
            when(tournamentRepository.save(tournament)).thenReturn(cancelledTournament);

            // When
            Tournament result = tournamentService.cancel(tournamentId);

            // Then
            assertNotNull(result);
            assertEquals(StatusTournament.CANCELLED, result.getStatus());

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentRepository).save(tournament);
        }

        @Test
        @DisplayName("Should throw exception when tournament not found")
        void shouldThrowExceptionWhenTournamentNotFound() {
            // Given
            Long tournamentId = 999L;
            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TournamentNotFoundException.class, () -> {
                tournamentService.cancel(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(tournamentRepository, never()).save(any(Tournament.class));
        }

        @Test
        @DisplayName("Should throw exception when tournament is already completed")
        void shouldThrowExceptionWhenTournamentAlreadyCompleted() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

            // When & Then
            assertThrows(InvalidTournamentStateException.class, () -> {
                tournamentService.cancel(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
        }
    }

    @Nested
    @DisplayName("Delete Tournament Tests")
    class DeleteTournamentTests {

        @Test
        @DisplayName("Should delete tournament successfully when status is CREATED")
        void shouldDeleteTournamentWhenStatusIsCreated() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
            doNothing().when(matchRepository).deleteByTournamentId(tournamentId);
            doNothing().when(teamRepository).deleteByTournamentId(tournamentId);
            doNothing().when(tournamentRepository).deleteById(tournamentId);

            // When
            tournamentService.delete(tournamentId);

            // Then
            verify(tournamentRepository).findById(tournamentId);
            verify(matchRepository).deleteByTournamentId(tournamentId);
            verify(teamRepository).deleteByTournamentId(tournamentId);
            verify(tournamentRepository).deleteById(tournamentId);
        }

        @Test
        @DisplayName("Should delete tournament successfully when status is COMPLETED")
        void shouldDeleteTournamentWhenStatusIsCompleted() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            tournament.startTournament();
            tournament.endTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
            doNothing().when(matchRepository).deleteByTournamentId(tournamentId);
            doNothing().when(teamRepository).deleteByTournamentId(tournamentId);
            doNothing().when(tournamentRepository).deleteById(tournamentId);

            // When
            tournamentService.delete(tournamentId);

            // Then
            verify(tournamentRepository).findById(tournamentId);
            verify(matchRepository).deleteByTournamentId(tournamentId);
            verify(teamRepository).deleteByTournamentId(tournamentId);
            verify(tournamentRepository).deleteById(tournamentId);
        }

        @Test
        @DisplayName("Should delete tournament successfully when status is CANCELLED")
        void shouldDeleteTournamentWhenStatusIsCancelled() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            tournament.cancelTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));
            doNothing().when(matchRepository).deleteByTournamentId(tournamentId);
            doNothing().when(teamRepository).deleteByTournamentId(tournamentId);
            doNothing().when(tournamentRepository).deleteById(tournamentId);

            // When
            tournamentService.delete(tournamentId);

            // Then
            verify(tournamentRepository).findById(tournamentId);
            verify(matchRepository).deleteByTournamentId(tournamentId);
            verify(teamRepository).deleteByTournamentId(tournamentId);
            verify(tournamentRepository).deleteById(tournamentId);
        }

        @Test
        @DisplayName("Should throw exception when tournament not found")
        void shouldThrowExceptionWhenTournamentNotFound() {
            // Given
            Long tournamentId = 999L;
            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TournamentNotFoundException.class, () -> {
                tournamentService.delete(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(matchRepository, never()).deleteByTournamentId(anyLong());
            verify(teamRepository, never()).deleteByTournamentId(anyLong());
            verify(tournamentRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw exception when tournament is IN_PROGRESS")
        void shouldThrowExceptionWhenTournamentIsInProgress() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            tournament.startTournament();

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

            // When & Then
            assertThrows(TournamentCannotBeDeletedException.class, () -> {
                tournamentService.delete(tournamentId);
            });

            verify(tournamentRepository).findById(tournamentId);
            verify(matchRepository, never()).deleteByTournamentId(anyLong());
            verify(teamRepository, never()).deleteByTournamentId(anyLong());
            verify(tournamentRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Get Tournament Tests")
    class GetTournamentTests {

        @Test
        @DisplayName("Should get tournament by id successfully")
        void shouldGetTournamentByIdSuccessfully() {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");

            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.of(tournament));

            // When
            Optional<Tournament> result = tournamentService.getById(tournamentId);

            // Then
            assertTrue(result.isPresent());
            assertEquals(tournamentId, result.get().getId());
            assertEquals("La Liga", result.get().getName());

            verify(tournamentRepository).findById(tournamentId);
        }

        @Test
        @DisplayName("Should return empty when tournament not found")
        void shouldReturnEmptyWhenTournamentNotFound() {
            // Given
            Long tournamentId = 999L;
            when(tournamentRepository.findById(tournamentId)).thenReturn(Optional.empty());

            // When
            Optional<Tournament> result = tournamentService.getById(tournamentId);

            // Then
            assertFalse(result.isPresent());
            verify(tournamentRepository).findById(tournamentId);
        }

        @Test
        @DisplayName("Should get all tournaments successfully")
        void shouldGetAllTournamentsSuccessfully() {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(2L, "Premier League", "English Football Championship");
            List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

            when(tournamentRepository.findAll()).thenReturn(tournaments);

            // When
            List<Tournament> result = tournamentService.getAll();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("La Liga", result.get(0).getName());
            assertEquals("Premier League", result.get(1).getName());

            verify(tournamentRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no tournaments exist")
        void shouldReturnEmptyListWhenNoTournamentsExist() {
            // Given
            when(tournamentRepository.findAll()).thenReturn(Arrays.asList());

            // When
            List<Tournament> result = tournamentService.getAll();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(tournamentRepository).findAll();
        }
    }
}
