package com.personal.tournament_api.tournament.application;

import com.personal.tournament_api.shared.domain.events.DomainEvent;
import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.tournament.domain.events.TournamentDeletedEvent;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentCannotBeDeletedException;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.domain.ports.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("DeleteTournamentService Tests")
@ExtendWith(MockitoExtension.class)
class DeleteTournamentServiceTest {

    @Mock private TournamentRepository tournamentRepository;
    @Mock private DomainEventPublisher domainEventPublisher;

    private DeleteTournamentService service;

    @BeforeEach
    void setUp() {
        service = new DeleteTournamentService(tournamentRepository, domainEventPublisher);
    }

    @Test
    @DisplayName("Should delete tournament and publish event when status is CREATED")
    void shouldDeleteTournamentWhenStatusIsCreated() {
        // Given
        Long id = 1L;
        Tournament tournament = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));

        // When
        service.delete(id);

        // Then
        verify(domainEventPublisher).publish(any(TournamentDeletedEvent.class));
        verify(tournamentRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should delete tournament and publish event when status is COMPLETED")
    void shouldDeleteTournamentWhenStatusIsCompleted() {
        // Given
        Long id = 1L;
        Tournament tournament = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        tournament.startTournament();
        tournament.endTournament();

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));

        // When
        service.delete(id);

        // Then
        verify(domainEventPublisher).publish(any(TournamentDeletedEvent.class));
        verify(tournamentRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should publish event with correct tournamentId")
    void shouldPublishEventWithCorrectTournamentId() {
        // Given
        Long id = 1L;
        Tournament tournament = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));
        ArgumentCaptor<DomainEvent> captor = ArgumentCaptor.forClass(DomainEvent.class);

        // When
        service.delete(id);

        // Then
        verify(domainEventPublisher).publish(captor.capture());
        assertEquals(id, ((TournamentDeletedEvent) captor.getValue()).tournamentId());
    }

    @Test
    @DisplayName("Should throw TournamentNotFoundException when tournament not found")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        when(tournamentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TournamentNotFoundException.class, () -> service.delete(999L));

        verify(domainEventPublisher, never()).publish(any());
        verify(tournamentRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw TournamentCannotBeDeletedException when tournament is IN_PROGRESS")
    void shouldThrowExceptionWhenTournamentIsInProgress() {
        // Given
        Long id = 1L;
        Tournament tournament = Tournament.reconstitute(id, "La Liga", "Spanish Championship", StatusTournament.CREATED);
        tournament.startTournament();

        when(tournamentRepository.findById(id)).thenReturn(Optional.of(tournament));

        // When & Then
        assertThrows(TournamentCannotBeDeletedException.class, () -> service.delete(id));

        verify(domainEventPublisher, never()).publish(any());
        verify(tournamentRepository, never()).deleteById(anyLong());
    }
}
