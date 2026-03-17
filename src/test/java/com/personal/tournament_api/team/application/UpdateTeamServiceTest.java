package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase.UpdateTeamCommand;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.exceptions.DuplicateTeamNameException;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
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

@DisplayName("UpdateTeamService Tests")
@ExtendWith(MockitoExtension.class)
class UpdateTeamServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private TeamDomainService teamDomainService;

    private UpdateTeamService service;

    @BeforeEach
    void setUp() {
        service = new UpdateTeamService(teamRepository, teamDomainService);
    }

    @Test
    @DisplayName("Should update team successfully")
    void shouldUpdateTeamSuccessfully() {
        // Given
        Team team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 1L);
        UpdateTeamCommand command = new UpdateTeamCommand(1L, "Real Madrid CF", "Carlo Ancelotti");

        doNothing().when(teamDomainService).validateUniqueNameForUpdate(eq("Real Madrid CF"), eq(1L), eq(teamRepository));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any())).thenReturn(team);

        // When
        Team result = service.update(command);

        // Then
        assertNotNull(result);
        verify(teamDomainService).validateUniqueNameForUpdate("Real Madrid CF", 1L, teamRepository);
        verify(teamRepository).save(team);
    }

    @Test
    @DisplayName("Should throw TeamNotFoundException when team not found")
    void shouldThrowExceptionWhenTeamNotFound() {
        // Given
        UpdateTeamCommand command = new UpdateTeamCommand(999L, "Real Madrid CF", "Carlo Ancelotti");

        doNothing().when(teamDomainService).validateUniqueNameForUpdate(anyString(), anyLong(), any());
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> service.update(command));
        verify(teamRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DuplicateTeamNameException when updating to existing name")
    void shouldThrowExceptionWhenUpdatingToExistingName() {
        // Given
        UpdateTeamCommand command = new UpdateTeamCommand(1L, "Barcelona", "Xavi");

        doThrow(new DuplicateTeamNameException("Barcelona"))
                .when(teamDomainService).validateUniqueNameForUpdate(eq("Barcelona"), eq(1L), any());

        // When & Then
        assertThrows(DuplicateTeamNameException.class, () -> service.update(command));
        verify(teamRepository, never()).save(any());
    }
}
