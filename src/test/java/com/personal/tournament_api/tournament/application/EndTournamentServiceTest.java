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

@DisplayName("EndTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class EndTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;

    private EndTournamentService service;

    @BeforeEach
    void setUp() {
        service = new EndTournamentService(tournamentRepository);
    }

    @Test
    @DisplayName("Should end tournament successfully")
    void shouldEndTournamentSuccessfully() {
        // Given
        Long id = 1L;
        Tournament tournament = new Tournament(id, "La Liga", "Spanish Championship");
        tournament.startTournament();
        Tournament ended = new Tournament(id, "La Liga", "Spanish Championship");
        ended.startTournament();
        ended.endTournament();

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));
        when(tournamentRepository.save(tournament)).thenReturn(ended);

        // When
        Tournament result = service.end(id);

        // Then
        assertEquals(StatusTournament.COMPLETED, result.getStatus());
        verify(tournamentRepository).save(tournament);
    }

    @Test
    @DisplayName("Should throw TournamentNotFoundException when tournament not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TournamentNotFoundException.class, () -> service.end(999L));
        verify(tournamentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidTournamentStateException when tournament is not IN_PROGRESS")
    void shouldThrowExceptionWhenNotInProgress() {
        // Given
        Long id = 1L;
        Tournament tournament = new Tournament(id, "La Liga", "Spanish Championship"); // CREATED

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));

        // When & Then
        assertThrows(InvalidTournamentStateException.class, () -> service.end(id));
    }
}
