package com.personal.tournament_api.tournament.infrastructure.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.tournament_api.tournament.application.usecases.*;
import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.DuplicateTournamentNameException;
import com.personal.tournament_api.tournament.domain.exceptions.InvalidTournamentStateException;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentCannotBeDeletedException;
import com.personal.tournament_api.tournament.domain.exceptions.TournamentNotFoundException;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.infrastructure.adapters.web.dto.TournamentRequest;
import com.personal.tournament_api.tournament.infrastructure.adapters.web.dto.TournamentResponse;
import com.personal.tournament_api.tournament.infrastructure.adapters.web.mapper.TournamentMapper;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TournamentController.class)
@DisplayName("Tournament Controller Integration Tests")
class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateTournamentUseCase createTournamentUseCase;

    @MockBean
    private UpdateTournamentUseCase updateTournamentUseCase;

    @MockBean
    private GetTournamentUseCase getTournamentUseCase;

    @MockBean
    private StartTournamentUseCase startTournamentUseCase;

    @MockBean
    private endTournamentUseCase endTournamentUseCase;

    @MockBean
    private CancelTournamentUseCase cancelTournamentUseCase;

    @MockBean
    private DeleteTournamentUseCase deleteTournamentUseCase;

    @MockBean
    private TournamentMapper tournamentMapper;

    @Nested
    @DisplayName("Create Tournament Tests")
    class CreateTournamentTests {

        @Test
        @DisplayName("Should create tournament successfully with valid request")
        void shouldCreateTournamentSuccessfully() throws Exception {
            // Given
            TournamentRequest request = new TournamentRequest("La Liga", "Spanish Football Championship");
            Tournament createdTournament = new Tournament(1L, "La Liga", "Spanish Football Championship");
            TournamentResponse response = new TournamentResponse(1L, "La Liga", "Spanish Football Championship", StatusTournament.CREATED);

            when(tournamentMapper.toCreateCommand(any(TournamentRequest.class)))
                    .thenReturn(new CreateTournamentUseCase.CreateTournamentCommand("La Liga", "Spanish Football Championship"));
            when(createTournamentUseCase.create(any())).thenReturn(createdTournament);
            when(tournamentMapper.toResponse(any(Tournament.class))).thenReturn(response);

            // When & Then
            mockMvc.perform(post("/tournaments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("La Liga"))
                    .andExpect(jsonPath("$.description").value("Spanish Football Championship"))
                    .andExpect(jsonPath("$.status").value("CREATED"));

            verify(createTournamentUseCase).create(any());
            verify(tournamentMapper).toResponse(createdTournament);
        }

        @Test
        @DisplayName("Should return 400 when name is blank")
        void shouldReturn400WhenNameIsBlank() throws Exception {
            // Given
            TournamentRequest request = new TournamentRequest("", "Valid description here");

            // When & Then
            mockMvc.perform(post("/tournaments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(createTournamentUseCase, never()).create(any());
        }

        @Test
        @DisplayName("Should return 400 when description is too short")
        void shouldReturn400WhenDescriptionIsTooShort() throws Exception {
            // Given
            TournamentRequest request = new TournamentRequest("La Liga", "Short");

            // When & Then
            mockMvc.perform(post("/tournaments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(createTournamentUseCase, never()).create(any());
        }

        @Test
        @DisplayName("Should return 422 when tournament name already exists")
        void shouldReturn422WhenTournamentNameAlreadyExists() throws Exception {
            // Given
            TournamentRequest request = new TournamentRequest("La Liga", "Spanish Football Championship");

            when(tournamentMapper.toCreateCommand(any(TournamentRequest.class)))
                    .thenReturn(new CreateTournamentUseCase.CreateTournamentCommand("La Liga", "Spanish Football Championship"));
            when(createTournamentUseCase.create(any())).thenThrow(new DuplicateTournamentNameException("La Liga"));

            // When & Then
            mockMvc.perform(post("/tournaments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity());

            verify(createTournamentUseCase).create(any());
        }
    }

    @Nested
    @DisplayName("Get Tournament Tests")
    class GetTournamentTests {

        @Test
        @DisplayName("Should get all tournaments successfully")
        void shouldGetAllTournamentsSuccessfully() throws Exception {
            // Given
            Tournament tournament1 = new Tournament(1L, "La Liga", "Spanish Football Championship");
            Tournament tournament2 = new Tournament(2L, "Premier League", "English Football Championship");
            List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

            TournamentResponse response1 = new TournamentResponse(1L, "La Liga", "Spanish Football Championship", StatusTournament.CREATED);
            TournamentResponse response2 = new TournamentResponse(2L, "Premier League", "English Football Championship", StatusTournament.CREATED);

            when(getTournamentUseCase.getAll()).thenReturn(tournaments);
            when(tournamentMapper.toResponseList(tournaments)).thenReturn(Arrays.asList(response1, response2));

            // When & Then
            mockMvc.perform(get("/tournaments"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name").value("La Liga"))
                    .andExpect(jsonPath("$[1].name").value("Premier League"));

            verify(getTournamentUseCase).getAll();
        }

        @Test
        @DisplayName("Should get tournament by id successfully")
        void shouldGetTournamentByIdSuccessfully() throws Exception {
            // Given
            Long tournamentId = 1L;
            Tournament tournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            TournamentResponse response = new TournamentResponse(tournamentId, "La Liga", "Spanish Football Championship", StatusTournament.CREATED);

            when(getTournamentUseCase.getById(tournamentId)).thenReturn(Optional.of(tournament));
            when(tournamentMapper.toResponse(tournament)).thenReturn(response);

            // When & Then
            mockMvc.perform(get("/tournaments/{id}", tournamentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("La Liga"));

            verify(getTournamentUseCase).getById(tournamentId);
        }

        @Test
        @DisplayName("Should return 404 when tournament not found")
        void shouldReturn404WhenTournamentNotFound() throws Exception {
            // Given
            Long tournamentId = 999L;
            when(getTournamentUseCase.getById(tournamentId)).thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get("/tournaments/{id}", tournamentId))
                    .andExpect(status().isNotFound());

            verify(getTournamentUseCase).getById(tournamentId);
        }
    }

    @Nested
    @DisplayName("Update Tournament Tests")
    class UpdateTournamentTests {

        @Test
        @DisplayName("Should update tournament successfully")
        void shouldUpdateTournamentSuccessfully() throws Exception {
            // Given
            Long tournamentId = 1L;
            TournamentRequest request = new TournamentRequest("La Liga Santander", "Updated Description Here");
            Tournament updatedTournament = new Tournament(tournamentId, "La Liga Santander", "Updated Description Here");
            TournamentResponse response = new TournamentResponse(tournamentId, "La Liga Santander", "Updated Description Here", StatusTournament.CREATED);

            when(tournamentMapper.toUpdateCommand(anyLong(), any(TournamentRequest.class)))
                    .thenReturn(new UpdateTournamentUseCase.UpdateTournamentCommand(tournamentId, "La Liga Santander", "Updated Description Here"));
            when(updateTournamentUseCase.update(any())).thenReturn(updatedTournament);
            when(tournamentMapper.toResponse(updatedTournament)).thenReturn(response);

            // When & Then
            mockMvc.perform(put("/tournaments/{id}", tournamentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("La Liga Santander"))
                    .andExpect(jsonPath("$.description").value("Updated Description Here"));

            verify(updateTournamentUseCase).update(any());
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent tournament")
        void shouldReturn404WhenUpdatingNonExistentTournament() throws Exception {
            // Given
            Long tournamentId = 999L;
            TournamentRequest request = new TournamentRequest("New Name", "New Description Here");

            when(tournamentMapper.toUpdateCommand(anyLong(), any(TournamentRequest.class)))
                    .thenReturn(new UpdateTournamentUseCase.UpdateTournamentCommand(tournamentId, "New Name", "New Description Here"));
            when(updateTournamentUseCase.update(any())).thenThrow(new TournamentNotFoundException(tournamentId));

            // When & Then
            mockMvc.perform(put("/tournaments/{id}", tournamentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());

            verify(updateTournamentUseCase).update(any());
        }

        @Test
        @DisplayName("Should return 422 when updated name already exists")
        void shouldReturn422WhenUpdatedNameAlreadyExists() throws Exception {
            // Given
            Long tournamentId = 1L;
            TournamentRequest request = new TournamentRequest("Premier League", "Spanish Football Championship");

            when(tournamentMapper.toUpdateCommand(anyLong(), any(TournamentRequest.class)))
                    .thenReturn(new UpdateTournamentUseCase.UpdateTournamentCommand(tournamentId, "Premier League", "Spanish Football Championship"));
            when(updateTournamentUseCase.update(any())).thenThrow(new DuplicateTournamentNameException("Premier League"));

            // When & Then
            mockMvc.perform(put("/tournaments/{id}", tournamentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity());

            verify(updateTournamentUseCase).update(any());
        }
    }

    @Nested
    @DisplayName("Start Tournament Tests")
    class StartTournamentTests {

        @Test
        @DisplayName("Should start tournament successfully")
        void shouldStartTournamentSuccessfully() throws Exception {
            // Given
            Long tournamentId = 1L;
            Tournament startedTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            startedTournament.startTournament();
            TournamentResponse response = new TournamentResponse(tournamentId, "La Liga", "Spanish Football Championship", StatusTournament.IN_PROGRESS);

            when(startTournamentUseCase.start(tournamentId)).thenReturn(startedTournament);
            when(tournamentMapper.toResponse(startedTournament)).thenReturn(response);

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/start", tournamentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

            verify(startTournamentUseCase).start(tournamentId);
        }

        @Test
        @DisplayName("Should return 404 when starting non-existent tournament")
        void shouldReturn404WhenStartingNonExistentTournament() throws Exception {
            // Given
            Long tournamentId = 999L;
            when(startTournamentUseCase.start(tournamentId)).thenThrow(new TournamentNotFoundException(tournamentId));

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/start", tournamentId))
                    .andExpect(status().isNotFound());

            verify(startTournamentUseCase).start(tournamentId);
        }

        @Test
        @DisplayName("Should return 400 when starting tournament with invalid state")
        void shouldReturn400WhenStartingTournamentWithInvalidState() throws Exception {
            // Given
            Long tournamentId = 1L;
            when(startTournamentUseCase.start(tournamentId))
                    .thenThrow(new InvalidTournamentStateException("Tournament can only be started if it is in created status"));

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/start", tournamentId))
                    .andExpect(status().isBadRequest());

            verify(startTournamentUseCase).start(tournamentId);
        }
    }

    @Nested
    @DisplayName("End Tournament Tests")
    class EndTournamentTests {

        @Test
        @DisplayName("Should end tournament successfully")
        void shouldEndTournamentSuccessfully() throws Exception {
            // Given
            Long tournamentId = 1L;
            Tournament endedTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            endedTournament.startTournament();
            endedTournament.endTournament();
            TournamentResponse response = new TournamentResponse(tournamentId, "La Liga", "Spanish Football Championship", StatusTournament.COMPLETED);

            when(endTournamentUseCase.end(tournamentId)).thenReturn(endedTournament);
            when(tournamentMapper.toResponse(endedTournament)).thenReturn(response);

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/end", tournamentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.status").value("COMPLETED"));

            verify(endTournamentUseCase).end(tournamentId);
        }

        @Test
        @DisplayName("Should return 404 when ending non-existent tournament")
        void shouldReturn404WhenEndingNonExistentTournament() throws Exception {
            // Given
            Long tournamentId = 999L;
            when(endTournamentUseCase.end(tournamentId)).thenThrow(new TournamentNotFoundException(tournamentId));

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/end", tournamentId))
                    .andExpect(status().isNotFound());

            verify(endTournamentUseCase).end(tournamentId);
        }

        @Test
        @DisplayName("Should return 400 when ending tournament not in progress")
        void shouldReturn400WhenEndingTournamentNotInProgress() throws Exception {
            // Given
            Long tournamentId = 1L;
            when(endTournamentUseCase.end(tournamentId))
                    .thenThrow(new InvalidTournamentStateException("Tournament can only be completed if it is in progress"));

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/end", tournamentId))
                    .andExpect(status().isBadRequest());

            verify(endTournamentUseCase).end(tournamentId);
        }
    }

    @Nested
    @DisplayName("Cancel Tournament Tests")
    class CancelTournamentTests {

        @Test
        @DisplayName("Should cancel tournament successfully")
        void shouldCancelTournamentSuccessfully() throws Exception {
            // Given
            Long tournamentId = 1L;
            Tournament cancelledTournament = new Tournament(tournamentId, "La Liga", "Spanish Football Championship");
            cancelledTournament.cancelTournament();
            TournamentResponse response = new TournamentResponse(tournamentId, "La Liga", "Spanish Football Championship", StatusTournament.CANCELLED);

            when(cancelTournamentUseCase.cancel(tournamentId)).thenReturn(cancelledTournament);
            when(tournamentMapper.toResponse(cancelledTournament)).thenReturn(response);

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/cancel", tournamentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.status").value("CANCELLED"));

            verify(cancelTournamentUseCase).cancel(tournamentId);
        }

        @Test
        @DisplayName("Should return 404 when cancelling non-existent tournament")
        void shouldReturn404WhenCancellingNonExistentTournament() throws Exception {
            // Given
            Long tournamentId = 999L;
            when(cancelTournamentUseCase.cancel(tournamentId)).thenThrow(new TournamentNotFoundException(tournamentId));

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/cancel", tournamentId))
                    .andExpect(status().isNotFound());

            verify(cancelTournamentUseCase).cancel(tournamentId);
        }

        @Test
        @DisplayName("Should return 400 when cancelling completed tournament")
        void shouldReturn400WhenCancellingCompletedTournament() throws Exception {
            // Given
            Long tournamentId = 1L;
            when(cancelTournamentUseCase.cancel(tournamentId))
                    .thenThrow(new InvalidTournamentStateException("Cannot cancel a completed tournament"));

            // When & Then
            mockMvc.perform(patch("/tournaments/{id}/cancel", tournamentId))
                    .andExpect(status().isBadRequest());

            verify(cancelTournamentUseCase).cancel(tournamentId);
        }
    }

    @Nested
    @DisplayName("Delete Tournament Tests")
    class DeleteTournamentTests {

        @Test
        @DisplayName("Should delete tournament successfully")
        void shouldDeleteTournamentSuccessfully() throws Exception {
            // Given
            Long tournamentId = 1L;
            doNothing().when(deleteTournamentUseCase).delete(tournamentId);

            // When & Then
            mockMvc.perform(delete("/tournaments/{id}", tournamentId))
                    .andExpect(status().isNoContent());

            verify(deleteTournamentUseCase).delete(tournamentId);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent tournament")
        void shouldReturn404WhenDeletingNonExistentTournament() throws Exception {
            // Given
            Long tournamentId = 999L;
            doThrow(new TournamentNotFoundException(tournamentId)).when(deleteTournamentUseCase).delete(tournamentId);

            // When & Then
            mockMvc.perform(delete("/tournaments/{id}", tournamentId))
                    .andExpect(status().isNotFound());

            verify(deleteTournamentUseCase).delete(tournamentId);
        }

        @Test
        @DisplayName("Should return 422 when deleting tournament in progress")
        void shouldReturn422WhenDeletingTournamentInProgress() throws Exception {
            // Given
            Long tournamentId = 1L;
            doThrow(new TournamentCannotBeDeletedException()).when(deleteTournamentUseCase).delete(tournamentId);

            // When & Then
            mockMvc.perform(delete("/tournaments/{id}", tournamentId))
                    .andExpect(status().isUnprocessableEntity());

            verify(deleteTournamentUseCase).delete(tournamentId);
        }
    }
}
