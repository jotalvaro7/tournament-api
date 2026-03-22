package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.application.usecases.CreateTournamentUseCase.CreateTournamentCommand;
import com.personal.tournament_api.tournament.domain.TournamentDomainService;
import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.DuplicateTournamentNameException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("CreateTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class CreateTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;
    @Mock private TournamentDomainService tournamentDomainService;

    private CreateTournamentService service;

    @BeforeEach
    void setUp() {
        service = new CreateTournamentService(tournamentRepository, tournamentDomainService);
    }

    @Test
    @DisplayName("Should create tournament successfully with valid data")
    void shouldCreateTournamentSuccessfully() {
        // Given
        CreateTournamentCommand command = new CreateTournamentCommand("La Liga", "Spanish Football Championship");
        Tournament saved = Tournament.reconstitute(1L, "La Liga", "Spanish Football Championship", StatusTournament.CREATED);

        doNothing().when(tournamentDomainService).validateUniqueName(anyString(), any());
        when(tournamentRepository.save(any())).thenReturn(saved);

        // When
        Tournament result = service.create(command);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("La Liga", result.getName());
        assertEquals(StatusTournament.CREATED, result.getStatus());

        verify(tournamentDomainService).validateUniqueName("La Liga", tournamentRepository);
        verify(tournamentRepository).save(any(Tournament.class));
    }

    @Test
    @DisplayName("Should throw DuplicateTournamentNameException when name already exists")
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Given
        CreateTournamentCommand command = new CreateTournamentCommand("La Liga", "Description");

        doThrow(new DuplicateTournamentNameException("La Liga"))
                .when(tournamentDomainService).validateUniqueName(anyString(), any());

        // When & Then
        assertThrows(DuplicateTournamentNameException.class, () -> service.create(command));

        verify(tournamentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save tournament with null id initially")
    void shouldSaveTournamentWithNullId() {
        // Given
        CreateTournamentCommand command = new CreateTournamentCommand("Serie A", "Italian Championship");
        Tournament saved = Tournament.reconstitute(1L, "Serie A", "Italian Championship", StatusTournament.CREATED);

        doNothing().when(tournamentDomainService).validateUniqueName(anyString(), any());
        when(tournamentRepository.save(any())).thenReturn(saved);

        ArgumentCaptor<Tournament> captor = ArgumentCaptor.forClass(Tournament.class);

        // When
        service.create(command);

        // Then
        verify(tournamentRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
    }
}
