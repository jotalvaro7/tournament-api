package com.personal.tournament_api.team.infrastructure.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase.CreateTeamCommand;
import com.personal.tournament_api.team.application.usecases.DeleteTeamUseCase;
import com.personal.tournament_api.team.application.usecases.GetTeamUseCase;
import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase;
import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase.UpdateTeamCommand;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamRequestDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamResponseDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.mapper.TeamMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
@DisplayName("TeamController Unit Tests")
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Note: @MockBean is deprecated since Spring Boot 3.4.0 but still functional
    // Will be replaced with @MockitoBean in future Spring Boot versions
    @MockBean
    private CreateTeamUseCase createTeamUseCase;

    @MockBean
    private GetTeamUseCase getTeamUseCase;

    @MockBean
    private UpdateTeamUseCase updateTeamUseCase;

    @MockBean
    private DeleteTeamUseCase deleteTeamUseCase;

    @MockBean
    private TeamMapper teamMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Team team;
    private TeamRequestDTO teamRequestDTO;
    private TeamResponseDTO teamResponseDTO;
    private CreateTeamCommand createTeamCommand;
    private UpdateTeamCommand updateTeamCommand;

    @BeforeEach
    void setUp() {
        team = new Team(1L, "Real Madrid", "Carlo Ancelotti", 1L);

        teamRequestDTO = new TeamRequestDTO("Real Madrid", "Carlo Ancelotti");

        teamResponseDTO = new TeamResponseDTO(
            1L,
            "Real Madrid",
            "Carlo Ancelotti",
            1L,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        );

        createTeamCommand = new CreateTeamCommand("Real Madrid", "Carlo Ancelotti", 1L);
        updateTeamCommand = new UpdateTeamCommand(1L, "Real Madrid", "Carlo Ancelotti");
    }

    @Nested
    @DisplayName("POST /tournaments/{tournamentId}/teams")
    class CreateTeamTests {

        @Test
        @DisplayName("Should create team successfully with valid data")
        void shouldCreateTeamSuccessfully() throws Exception {
            when(teamMapper.toCreateCommand(eq(1L), any(TeamRequestDTO.class))).thenReturn(createTeamCommand);
            when(createTeamUseCase.create(any(CreateTeamCommand.class))).thenReturn(team);
            when(teamMapper.toResponse(any(Team.class))).thenReturn(teamResponseDTO);

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(teamRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Real Madrid"))
                .andExpect(jsonPath("$.coach").value("Carlo Ancelotti"))
                .andExpect(jsonPath("$.tournamentId").value(1))
                .andExpect(jsonPath("$.points").value(0));

            verify(teamMapper, times(1)).toCreateCommand(eq(1L), any(TeamRequestDTO.class));
            verify(createTeamUseCase, times(1)).create(any(CreateTeamCommand.class));
            verify(teamMapper, times(1)).toResponse(any(Team.class));
        }

        @Test
        @DisplayName("Should return 400 when name is empty")
        void shouldReturn400WhenNameIsEmpty() throws Exception {
            TeamRequestDTO invalidRequest = new TeamRequestDTO("", "Carlo Ancelotti");

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createTeamUseCase, never()).create(any(CreateTeamCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when name is too short")
        void shouldReturn400WhenNameIsTooShort() throws Exception {
            TeamRequestDTO invalidRequest = new TeamRequestDTO("AB", "Carlo Ancelotti");

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createTeamUseCase, never()).create(any(CreateTeamCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when name is too long")
        void shouldReturn400WhenNameIsTooLong() throws Exception {
            String longName = "A".repeat(101);
            TeamRequestDTO invalidRequest = new TeamRequestDTO(longName, "Carlo Ancelotti");

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createTeamUseCase, never()).create(any(CreateTeamCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when coach is empty")
        void shouldReturn400WhenCoachIsEmpty() throws Exception {
            TeamRequestDTO invalidRequest = new TeamRequestDTO("Real Madrid", "");

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createTeamUseCase, never()).create(any(CreateTeamCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when coach is too short")
        void shouldReturn400WhenCoachIsTooShort() throws Exception {
            TeamRequestDTO invalidRequest = new TeamRequestDTO("Real Madrid", "AB");

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createTeamUseCase, never()).create(any(CreateTeamCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when coach is too long")
        void shouldReturn400WhenCoachIsTooLong() throws Exception {
            String longCoach = "A".repeat(101);
            TeamRequestDTO invalidRequest = new TeamRequestDTO("Real Madrid", longCoach);

            mockMvc.perform(post("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createTeamUseCase, never()).create(any(CreateTeamCommand.class));
        }
    }

    @Nested
    @DisplayName("GET /tournaments/{tournamentId}/teams/{teamId}")
    class GetTeamByIdTests {

        @Test
        @DisplayName("Should get team by id successfully")
        void shouldGetTeamByIdSuccessfully() throws Exception {
            when(getTeamUseCase.getById(1L)).thenReturn(Optional.of(team));
            when(teamMapper.toResponse(any(Team.class))).thenReturn(teamResponseDTO);

            mockMvc.perform(get("/tournaments/1/teams/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Real Madrid"))
                .andExpect(jsonPath("$.coach").value("Carlo Ancelotti"));

            verify(getTeamUseCase, times(1)).getById(1L);
            verify(teamMapper, times(1)).toResponse(any(Team.class));
        }

        @Test
        @DisplayName("Should return 404 when team not found")
        void shouldReturn404WhenTeamNotFound() throws Exception {
            when(getTeamUseCase.getById(999L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/tournaments/1/teams/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(getTeamUseCase, times(1)).getById(999L);
            verify(teamMapper, never()).toResponse(any(Team.class));
        }
    }

    @Nested
    @DisplayName("GET /tournaments/{tournamentId}/teams")
    class GetAllTeamsTests {

        @Test
        @DisplayName("Should get all teams ordered by name ascending")
        void shouldGetAllTeamsOrderedByNameAsc() throws Exception {
            Team team1 = new Team(1L, "Barcelona", "Xavi Hernandez", 1L);
            Team team2 = new Team(2L, "Real Madrid", "Carlo Ancelotti", 1L);
            Team team3 = new Team(3L, "Sevilla", "Jose Luis Mendilibar", 1L);
            List<Team> teams = Arrays.asList(team1, team2, team3);

            TeamResponseDTO response1 = new TeamResponseDTO(1L, "Barcelona", "Xavi Hernandez", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
            TeamResponseDTO response2 = new TeamResponseDTO(2L, "Real Madrid", "Carlo Ancelotti", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
            TeamResponseDTO response3 = new TeamResponseDTO(3L, "Sevilla", "Jose Luis Mendilibar", 1L, 0, 0, 0, 0, 0, 0, 0, 0);
            List<TeamResponseDTO> responses = Arrays.asList(response1, response2, response3);

            when(getTeamUseCase.getAllByTournamentIdOrderByNameAsc(1L)).thenReturn(teams);
            when(teamMapper.toResponseList(teams)).thenReturn(responses);

            mockMvc.perform(get("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("Barcelona"))
                .andExpect(jsonPath("$[1].name").value("Real Madrid"))
                .andExpect(jsonPath("$[2].name").value("Sevilla"));

            verify(getTeamUseCase, times(1)).getAllByTournamentIdOrderByNameAsc(1L);
            verify(teamMapper, times(1)).toResponseList(teams);
        }

        @Test
        @DisplayName("Should return empty list when no teams exist")
        void shouldReturnEmptyListWhenNoTeamsExist() throws Exception {
            List<Team> emptyList = Arrays.asList();
            when(getTeamUseCase.getAllByTournamentIdOrderByNameAsc(1L)).thenReturn(emptyList);
            when(teamMapper.toResponseList(emptyList)).thenReturn(Arrays.asList());

            mockMvc.perform(get("/tournaments/1/teams")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

            verify(getTeamUseCase, times(1)).getAllByTournamentIdOrderByNameAsc(1L);
        }
    }

    @Nested
    @DisplayName("PUT /tournaments/{tournamentId}/teams/{teamId}")
    class UpdateTeamTests {

        @Test
        @DisplayName("Should update team successfully with valid data")
        void shouldUpdateTeamSuccessfully() throws Exception {
            TeamRequestDTO updateRequest = new TeamRequestDTO("Real Madrid CF", "Carlo Ancelotti Updated");
            Team updatedTeam = new Team(1L, "Real Madrid CF", "Carlo Ancelotti Updated", 1L);
            TeamResponseDTO updatedResponse = new TeamResponseDTO(
                1L, "Real Madrid CF", "Carlo Ancelotti Updated", 1L, 0, 0, 0, 0, 0, 0, 0, 0
            );

            when(teamMapper.toUpdateCommand(eq(1L), any(TeamRequestDTO.class))).thenReturn(updateTeamCommand);
            when(updateTeamUseCase.update(any(UpdateTeamCommand.class))).thenReturn(updatedTeam);
            when(teamMapper.toResponse(any(Team.class))).thenReturn(updatedResponse);

            mockMvc.perform(put("/tournaments/1/teams/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Real Madrid CF"))
                .andExpect(jsonPath("$.coach").value("Carlo Ancelotti Updated"));

            verify(teamMapper, times(1)).toUpdateCommand(eq(1L), any(TeamRequestDTO.class));
            verify(updateTeamUseCase, times(1)).update(any(UpdateTeamCommand.class));
            verify(teamMapper, times(1)).toResponse(any(Team.class));
        }

        @Test
        @DisplayName("Should return 400 when update with invalid name")
        void shouldReturn400WhenUpdateWithInvalidName() throws Exception {
            TeamRequestDTO invalidRequest = new TeamRequestDTO("", "Carlo Ancelotti");

            mockMvc.perform(put("/tournaments/1/teams/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(updateTeamUseCase, never()).update(any(UpdateTeamCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when update with invalid coach")
        void shouldReturn400WhenUpdateWithInvalidCoach() throws Exception {
            TeamRequestDTO invalidRequest = new TeamRequestDTO("Real Madrid", "");

            mockMvc.perform(put("/tournaments/1/teams/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(updateTeamUseCase, never()).update(any(UpdateTeamCommand.class));
        }

        @Test
        @DisplayName("Should invoke use case when updating team")
        void shouldInvokeUseCaseWhenUpdatingTeam() throws Exception {
            when(teamMapper.toUpdateCommand(eq(999L), any(TeamRequestDTO.class))).thenReturn(updateTeamCommand);
            when(updateTeamUseCase.update(any(UpdateTeamCommand.class))).thenReturn(team);
            when(teamMapper.toResponse(any(Team.class))).thenReturn(teamResponseDTO);

            mockMvc.perform(put("/tournaments/1/teams/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(teamRequestDTO)))
                .andExpect(status().isOk());

            verify(teamMapper, times(1)).toUpdateCommand(eq(999L), any(TeamRequestDTO.class));
            verify(updateTeamUseCase, times(1)).update(any(UpdateTeamCommand.class));
        }
    }

    @Nested
    @DisplayName("DELETE /tournaments/{tournamentId}/teams/{teamId}")
    class DeleteTeamTests {

        @Test
        @DisplayName("Should delete team successfully")
        void shouldDeleteTeamSuccessfully() throws Exception {
            doNothing().when(deleteTeamUseCase).delete(1L);

            mockMvc.perform(delete("/tournaments/1/teams/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(deleteTeamUseCase, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Should invoke use case when deleting team")
        void shouldInvokeUseCaseWhenDeletingTeam() throws Exception {
            doNothing().when(deleteTeamUseCase).delete(999L);

            mockMvc.perform(delete("/tournaments/1/teams/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(deleteTeamUseCase, times(1)).delete(999L);
        }
    }
}
