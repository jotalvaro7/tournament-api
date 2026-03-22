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

@DisplayName("StartTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class StartTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;

    private StartTournamentService service;

    @BeforeEach
    void setUp() {
        service = new StartTournamentService(tournamentRepository);
    }

    @Test
    @DisplayName("Should start tournament successfully")
    void shouldStartTournamentSuccessfully() {
        // Given
        Long id = 1L;
        Tournament tournament = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        Tournament started = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        started.startTournament();

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(started);

        // When
        Tournament result = service.start(id);

        // Then
        assertEquals(StatusTournament.IN_PROGRESS, result.getStatus());
        verify(tournamentRepository).save(tournament);
    }

    @Test
    @DisplayName("Should throw TournamentNotFoundException when tournament not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TournamentNotFoundException.class, () -> service.start(999L));
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidTournamentStateException when tournament is not CREATED")
    void shouldThrowExceptionWhenNotInCreatedStatus() {
        // Given
        Long id = 1L;
        Tournament tournament = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        tournament.startTournament(); // already IN_PROGRESS

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));

        // When & Then
        assertThrows(InvalidTournamentStateException.class, () -> service.start(id));
    }
}
