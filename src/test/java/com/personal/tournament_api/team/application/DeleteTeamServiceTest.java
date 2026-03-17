package com.personal.tournament_api.team.application;

import com.personal.tournament_api.shared.domain.events.DomainEvent;
import com.personal.tournament_api.shared.domain.ports.DomainEventPublisher;
import com.personal.tournament_api.team.domain.events.TeamDeletedEvent;
import com.personal.tournament_api.team.domain.exceptions.TeamHasMatchesException;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamMatchesPort;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
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
import static org.mockito.Mockito.*;

@DisplayName("DeleteTeamService Tests")
@ExtendWith(MockitoExtension.class)
class DeleteTeamServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private TeamMatchesPort teamMatchesPort;
    @Mock private DomainEventPublisher domainEventPublisher;

    private DeleteTeamService service;

    @BeforeEach
    void setUp() {
        service = new DeleteTeamService(teamRepository, teamMatchesPort, domainEventPublisher);
    }

    @Test
    @DisplayName("Should delete team and publish event when no associated matches")
    void shouldDeleteTeamSuccessfully() {
        // Given
        Team team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMatchesPort.countByTeamId(1L)).thenReturn(0);

        // When
        service.delete(1L);

        // Then
        verify(teamMatchesPort).countByTeamId(1L);
        verify(domainEventPublisher).publish(any(TeamDeletedEvent.class));
        verify(teamRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should publish event with correct teamId")
    void shouldPublishEventWithCorrectTeamId() {
        // Given
        Team team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMatchesPort.countByTeamId(1L)).thenReturn(0);
        ArgumentCaptor<DomainEvent> captor = ArgumentCaptor.forClass(DomainEvent.class);

        // When
        service.delete(1L);

        // Then
        verify(domainEventPublisher).publish(captor.capture());
        assertEquals(1L, ((TeamDeletedEvent) captor.getValue()).teamId());
    }

    @Test
    @DisplayName("Should throw TeamNotFoundException when team not found")
    void shouldThrowExceptionWhenTeamNotFound() {
        // Given
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> service.delete(999L));

        verify(teamMatchesPort, never()).countByTeamId(any());
        verify(domainEventPublisher, never()).publish(any());
        verify(teamRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw TeamHasMatchesException when team has associated matches")
    void shouldThrowExceptionWhenTeamHasMatches() {
        // Given
        Team team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 1L);

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamMatchesPort.countByTeamId(1L)).thenReturn(2);

        // When & Then
        TeamHasMatchesException exception = assertThrows(TeamHasMatchesException.class,
                () -> service.delete(1L));

        assertTrue(exception.getMessage().contains("cannot be deleted"));
        assertTrue(exception.getMessage().contains("2 associated match(es)"));

        verify(domainEventPublisher, never()).publish(any());
        verify(teamRepository, never()).deleteById(any());
    }
}
