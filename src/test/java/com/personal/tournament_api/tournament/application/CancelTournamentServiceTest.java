package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.InvalidTournamentStateException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CancelTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class CancelTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;

    private CancelTournamentService service;

    @BeforeEach
    void setUp() {
        service = new CancelTournamentService(tournamentRepository);
    }

    @Test
    @DisplayName("Should cancel tournament successfully")
    void shouldCancelTournamentSuccessfully() {
        // Given
        Long id = 1L;
        Tournament tournament = new Tournament(id, "La Liga", "Spanish Championship");
        Tournament cancelled = new Tournament(id, "La Liga", "Spanish Championship");
        cancelled.cancelTournament();

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(cancelled);

        // When
        Tournament result = service.cancel(id);

        // Then
        assertEquals(StatusTournament.CANCELLED, result.getStatus());
        verify(tournamentRepository).save(tournament);
    }

    @Test
    @DisplayName("Should throw TournamentNotFoundException when tournament not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TournamentNotFoundException.class, () -> service.cancel(999L));
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidTournamentStateException when tournament is COMPLETED")
    void shouldThrowExceptionWhenAlreadyCompleted() {
        // Given
        Long id = 1L;
        Tournament tournament = new Tournament(id, "La Liga", "Spanish Championship");
        tournament.startTournament();
        tournament.endTournament();

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));

        // When & Then
        assertThrows(InvalidTournamentStateException.class, () -> service.cancel(id));
    }
}
