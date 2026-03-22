package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.UpdateTournamentUseCase.UpdateTournamentCommand;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.DuplicateTournamentNameException;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("UpdateTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class UpdateTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;
    @Mock private TournamentDomainService tournamentDomainService;

    private UpdateTournamentService service;

    @BeforeEach
    void setUp() {
        service = new UpdateTournamentService(tournamentRepository, tournamentDomainService);
    }

    @Test
    @DisplayName("Should update tournament successfully")
    void shouldUpdateTournamentSuccessfully() {
        // Given
        Long id = 1L;
        UpdateTournamentCommand command = new UpdateTournamentCommand(id, "La Liga Santander", "Updated");
        Tournament existing = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        Tournament updated = Tournament.reconstitute(id, "La Liga Santander", "Updated", StatusTournament.CREATED);

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(tournamentDomainService).validateUniqueNameForUpdate(anyString(), anyLong(), any());
        when(tournamentRepository.save(any())).thenReturn(updated);

        // When
        Tournament result = service.update(command);

        // Then
        assertNotNull(result);
        assertEquals("La Liga Santander", result.getName());
        verify(tournamentDomainService).validateUniqueNameForUpdate("La Liga Santander", id, tournamentRepository);
        verify(tournamentRepository).save(existing);
    }

    @Test
    @DisplayName("Should throw TournamentNotFoundException when tournament not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        UpdateTournamentCommand command = new UpdateTournamentCommand(999L, "New Name", "Description");
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TournamentNotFoundException.class, () -> service.update(command));

        verify(tournamentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DuplicateTournamentNameException when name already exists")
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Given
        Long id = 1L;
        UpdateTournamentCommand command = new UpdateTournamentCommand(id, "Premier League", "Description");
        Tournament existing = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(existing));
        doThrow(new DuplicateTournamentNameException("Premier League"))
                .when(tournamentDomainService).validateUniqueNameForUpdate(anyString(), anyLong(), any());

        // When & Then
        assertThrows(DuplicateTournamentNameException.class, () -> service.update(command));

        verify(tournamentRepository, never()).save(any());
    }
}
