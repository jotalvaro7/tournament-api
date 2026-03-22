package com.personal.tournament_api.team.application;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase.CreateTeamCommand;
import com.personal.tournament_api.team.domain.TeamDomainService;
import com.personal.tournament_api.team.domain.exceptions.DuplicateTeamNameException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("CreateTeamService Tests")
@ExtendWith(MockitoExtension.class)
class CreateTeamServiceTest {

    @Mock private TeamRepository teamRepository;
    @Mock private TeamDomainService teamDomainService;

    private CreateTeamService service;

    @BeforeEach
    void setUp() {
        service = new CreateTeamService(teamRepository, teamDomainService);
    }

    @Test
    @DisplayName("Should create team successfully")
    void shouldCreateTeamSuccessfully() {
        // Given
        CreateTeamCommand command = new CreateTeamCommand("Real Madrid", "Carlo Ancelotti", 1L);
        Team saved = Team.reconstitute(1L, "Real Madrid", "Carlo Ancelotti", 1L, 0, 0, 0, 0, 0, 0, 0, 0);

        doNothing().when(teamDomainService).validateUniqueTeamName(eq("Real Madrid"), eq(teamRepository));
        when(teamRepository.save(any())).thenReturn(saved);

        // When
        Team result = service.create(command);

        // Then
        assertNotNull(result);
        assertEquals("Real Madrid", result.getName());
        assertEquals("Carlo Ancelotti", result.getCoach());

        verify(teamDomainService).validateUniqueTeamName("Real Madrid", teamRepository);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    @DisplayName("Should throw DuplicateTeamNameException when name already exists")
    void shouldThrowExceptionWhenNameExists() {
        // Given
        CreateTeamCommand command = new CreateTeamCommand("Real Madrid", "Carlo Ancelotti", 1L);

        doThrow(new DuplicateTeamNameException("Real Madrid"))
                .when(teamDomainService).validateUniqueTeamName(eq("Real Madrid"), eq(teamRepository));

        // When & Then
        assertThrows(DuplicateTeamNameException.class, () -> service.create(command));
        verify(teamRepository, never()).save(any());
    }
}
