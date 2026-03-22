package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GetTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class GetTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;

    private GetTournamentService service;

    @BeforeEach
    void setUp() {
        service = new GetTournamentService(tournamentRepository);
    }

    @Test
    @DisplayName("Should get tournament by id successfully")
    void shouldGetTournamentByIdSuccessfully() {
        // Given
        Tournament tournament = Tournament.reconstitute(1L, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        // When
        Optional<Tournament> result = service.getById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("La Liga", result.get().getName());
        verify(tournamentRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when tournament not found")
    void shouldReturnEmptyWhenNotFound() {
        // Given
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Tournament> result = service.getById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should get all tournaments successfully")
    void shouldGetAllTournamentsSuccessfully() {
        // Given
        List<Tournament> tournaments = List.of(
                Tournament.reconstitute(1L, "La Liga", "Spanish Championship", StatusTournament.CREATED),
                Tournament.reconstitute(2L, "Premier League", "English Championship", StatusTournament.CREATED)
        );
        when(tournamentRepository.findAll()).thenReturn(tournaments);

        // When
        List<Tournament> result = service.getAll();

        // Then
        assertEquals(2, result.size());
        verify(tournamentRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no tournaments exist")
    void shouldReturnEmptyList() {
        // Given
        when(tournamentRepository.findAll()).thenReturn(List.of());

        // When
        List<Tournament> result = service.getAll();

        // Then
        assertTrue(result.isEmpty());
    }
}
