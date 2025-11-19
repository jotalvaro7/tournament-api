package com.personal.tournament_api.match.infrastructure.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.model.*;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.FinishMatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchResponseDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.mapper.MatchFilterBuilder;
import com.personal.tournament_api.match.infrastructure.adapters.web.mapper.MatchMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
@DisplayName("MatchController Unit Tests")
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Note: @MockBean is deprecated since Spring Boot 3.4.0 but still functional
    // Will be replaced with @MockitoBean in future Spring Boot versions
    @MockBean
    private CreateMatchUseCase createMatchUseCase;

    @MockBean
    private UpdateMatchUseCase updateMatchUseCase;

    @MockBean
    private FinishMatchUseCase finishMatchUseCase;

    @MockBean
    private GetMatchUseCase getMatchUseCase;

    @MockBean
    private DeleteMatchUseCase deleteMatchUseCase;

    @MockBean
    private PostponeMatchUseCase postponeMatchUseCase;

    @MockBean
    private MatchMapper matchMapper;

    @MockBean
    private MatchFilterBuilder matchFilterBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    private Match match;
    private MatchRequestDTO matchRequestDTO;
    private MatchResponseDTO matchResponseDTO;
    private FinishMatchRequestDTO finishMatchRequestDTO;
    private CreateMatchUseCase.CreateMatchCommand createMatchCommand;
    private UpdateMatchUseCase.UpdateMatchCommand updateMatchCommand;
    private FinishMatchUseCase.FinishMatchCommand finishMatchCommand;

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2025, 11, 15, 15, 0);

    @BeforeEach
    void setUp() {
        match = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");

        matchRequestDTO = new MatchRequestDTO(1L, 2L, TEST_DATE, "Stadium A");

        matchResponseDTO = new MatchResponseDTO(
            1L,
            10L,
            1L,
            2L,
            null,
            null,
            TEST_DATE,
            "Stadium A",
            MatchStatus.SCHEDULED
        );

        finishMatchRequestDTO = new FinishMatchRequestDTO(3, 1);

        createMatchCommand = new CreateMatchUseCase.CreateMatchCommand(10L, 1L, 2L, TEST_DATE, "Stadium A");
        updateMatchCommand = new UpdateMatchUseCase.UpdateMatchCommand(1L, TEST_DATE, "Stadium A");
        finishMatchCommand = new FinishMatchUseCase.FinishMatchCommand(1L, 3, 1);

        // Configure default behavior for MatchFilterBuilder
        when(matchFilterBuilder.buildSearchCriteria(any(), any(), any(), any()))
                .thenAnswer(invocation -> {
                    Object specificDate = invocation.getArgument(0);
                    Object dateFrom = invocation.getArgument(1);
                    Object dateTo = invocation.getArgument(2);
                    MatchStatus status = invocation.getArgument(3);

                    if (specificDate != null) {
                        return MatchSearchCriteria.withSpecificDate((java.time.LocalDate) specificDate, status);
                    } else if (dateFrom != null && dateTo != null) {
                        return MatchSearchCriteria.withDateRange((java.time.LocalDate) dateFrom, (java.time.LocalDate) dateTo, status);
                    } else if (status != null) {
                        return MatchSearchCriteria.withStatus(status);
                    } else {
                        return MatchSearchCriteria.empty();
                    }
                });

        when(matchFilterBuilder.buildPageRequest(anyInt(), anyInt(), anyString(), anyString()))
                .thenAnswer(invocation -> {
                    int page = invocation.getArgument(0);
                    int size = invocation.getArgument(1);
                    String sortBy = invocation.getArgument(2);
                    String direction = invocation.getArgument(3);
                    PageRequest.SortDirection sortDirection = "DESC".equalsIgnoreCase(direction)
                            ? PageRequest.SortDirection.DESC
                            : PageRequest.SortDirection.ASC;
                    return PageRequest.of(page, size, sortBy, sortDirection);
                });
    }

    @Nested
    @DisplayName("POST /tournaments/{tournamentId}/matches")
    class CreateMatchTests {

        @Test
        @DisplayName("Should create match successfully with valid data")
        void shouldCreateMatchSuccessfully() throws Exception {
            when(matchMapper.toCreateCommand(eq(10L), any(MatchRequestDTO.class))).thenReturn(createMatchCommand);
            when(createMatchUseCase.create(any(CreateMatchUseCase.CreateMatchCommand.class))).thenReturn(match);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(matchResponseDTO);

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(matchRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tournamentId").value(10))
                .andExpect(jsonPath("$.homeTeamId").value(1))
                .andExpect(jsonPath("$.awayTeamId").value(2))
                .andExpect(jsonPath("$.field").value("Stadium A"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

            verify(matchMapper, times(1)).toCreateCommand(eq(10L), any(MatchRequestDTO.class));
            verify(createMatchUseCase, times(1)).create(any(CreateMatchUseCase.CreateMatchCommand.class));
            verify(matchMapper, times(1)).toResponse(any(Match.class));
        }

        @Test
        @DisplayName("Should return 400 when homeTeamId is null")
        void shouldReturn400WhenHomeTeamIdIsNull() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(null, 2L, TEST_DATE, "Stadium A");

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when homeTeamId is not positive")
        void shouldReturn400WhenHomeTeamIdIsNotPositive() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(0L, 2L, TEST_DATE, "Stadium A");

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when awayTeamId is null")
        void shouldReturn400WhenAwayTeamIdIsNull() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(1L, null, TEST_DATE, "Stadium A");

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when awayTeamId is not positive")
        void shouldReturn400WhenAwayTeamIdIsNotPositive() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(1L, 0L, TEST_DATE, "Stadium A");

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when matchDate is null")
        void shouldReturn400WhenMatchDateIsNull() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(1L, 2L, null, "Stadium A");

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when field is blank")
        void shouldReturn400WhenFieldIsBlank() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(1L, 2L, TEST_DATE, "");

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when field exceeds max length")
        void shouldReturn400WhenFieldExceedsMaxLength() throws Exception {
            String longField = "A".repeat(101);
            MatchRequestDTO invalidRequest = new MatchRequestDTO(1L, 2L, TEST_DATE, longField);

            mockMvc.perform(post("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(createMatchUseCase, never()).create(any(CreateMatchUseCase.CreateMatchCommand.class));
        }
    }

    @Nested
    @DisplayName("GET /tournaments/{tournamentId}/matches/{matchId}")
    class GetMatchByIdTests {

        @Test
        @DisplayName("Should get match by id successfully")
        void shouldGetMatchByIdSuccessfully() throws Exception {
            when(getMatchUseCase.getById(1L)).thenReturn(Optional.of(match));
            when(matchMapper.toResponse(any(Match.class))).thenReturn(matchResponseDTO);

            mockMvc.perform(get("/tournaments/10/matches/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tournamentId").value(10))
                .andExpect(jsonPath("$.homeTeamId").value(1))
                .andExpect(jsonPath("$.awayTeamId").value(2));

            verify(getMatchUseCase, times(1)).getById(1L);
            verify(matchMapper, times(1)).toResponse(any(Match.class));
        }

        @Test
        @DisplayName("Should return 404 when match not found")
        void shouldReturn404WhenMatchNotFound() throws Exception {
            when(getMatchUseCase.getById(999L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/tournaments/10/matches/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(getMatchUseCase, times(1)).getById(999L);
            verify(matchMapper, never()).toResponse(any(Match.class));
        }
    }

    @Nested
    @DisplayName("GET /tournaments/{tournamentId}/matches")
    class GetAllMatchesTests {

        @Test
        @DisplayName("Should get paginated matches for tournament without filters")
        void shouldGetPaginatedMatchesForTournament() throws Exception {
            Match match1 = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
            Match match2 = new Match(2L, 10L, 3L, 4L, TEST_DATE.plusDays(1), "Stadium B");
            List<Match> matches = Arrays.asList(match1, match2);

            MatchResponseDTO response1 = new MatchResponseDTO(1L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            MatchResponseDTO response2 = new MatchResponseDTO(2L, 10L, 3L, 4L, null, null, TEST_DATE.plusDays(1), "Stadium B", MatchStatus.SCHEDULED);
            List<MatchResponseDTO> responses = Arrays.asList(response1, response2);

            Page<Match> matchPage = new Page<>(matches, 0, 20, 2);
            Page<MatchResponseDTO> dtoPage = new Page<>(responses, 0, 20, 2);

            when(getMatchUseCase.getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class)))
                    .thenReturn(matchPage);
            when(matchMapper.toResponsePage(matchPage)).thenReturn(dtoPage);

            mockMvc.perform(get("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].field").value("Stadium A"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].field").value("Stadium B"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));

            verify(getMatchUseCase, times(1)).getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class));
            verify(matchMapper, times(1)).toResponsePage(matchPage);
        }

        @Test
        @DisplayName("Should return empty page when no matches exist")
        void shouldReturnEmptyPageWhenNoMatchesExist() throws Exception {
            List<Match> emptyList = Arrays.asList();
            List<MatchResponseDTO> emptyDTOList = Arrays.asList();
            Page<Match> emptyPage = new Page<>(emptyList, 0, 20, 0);
            Page<MatchResponseDTO> emptyDTOPage = new Page<>(emptyDTOList, 0, 20, 0);

            when(getMatchUseCase.getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class)))
                    .thenReturn(emptyPage);
            when(matchMapper.toResponsePage(emptyPage)).thenReturn(emptyDTOPage);

            mockMvc.perform(get("/tournaments/10/matches")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));

            verify(getMatchUseCase, times(1)).getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class));
            verify(matchMapper, times(1)).toResponsePage(emptyPage);
        }

        @Test
        @DisplayName("Should get matches with specific date filter")
        void shouldGetMatchesWithSpecificDateFilter() throws Exception {
            Match match1 = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
            List<Match> matches = Arrays.asList(match1);

            MatchResponseDTO response1 = new MatchResponseDTO(1L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            List<MatchResponseDTO> responses = Arrays.asList(response1);

            Page<Match> matchPage = new Page<>(matches, 0, 20, 1);
            Page<MatchResponseDTO> dtoPage = new Page<>(responses, 0, 20, 1);

            when(getMatchUseCase.getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class)))
                    .thenReturn(matchPage);
            when(matchMapper.toResponsePage(matchPage)).thenReturn(dtoPage);

            mockMvc.perform(get("/tournaments/10/matches")
                    .param("specificDate", "2025-11-15")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));

            verify(getMatchUseCase, times(1)).getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class));
            verify(matchMapper, times(1)).toResponsePage(matchPage);
        }

        @Test
        @DisplayName("Should get matches with date range filter")
        void shouldGetMatchesWithDateRangeFilter() throws Exception {
            Match match1 = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
            Match match2 = new Match(2L, 10L, 3L, 4L, TEST_DATE.plusDays(1), "Stadium B");
            List<Match> matches = Arrays.asList(match1, match2);

            MatchResponseDTO response1 = new MatchResponseDTO(1L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            MatchResponseDTO response2 = new MatchResponseDTO(2L, 10L, 3L, 4L, null, null, TEST_DATE.plusDays(1), "Stadium B", MatchStatus.SCHEDULED);
            List<MatchResponseDTO> responses = Arrays.asList(response1, response2);

            Page<Match> matchPage = new Page<>(matches, 0, 20, 2);
            Page<MatchResponseDTO> dtoPage = new Page<>(responses, 0, 20, 2);

            when(getMatchUseCase.getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class)))
                    .thenReturn(matchPage);
            when(matchMapper.toResponsePage(matchPage)).thenReturn(dtoPage);

            mockMvc.perform(get("/tournaments/10/matches")
                    .param("dateFrom", "2025-11-15")
                    .param("dateTo", "2025-11-20")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

            verify(getMatchUseCase, times(1)).getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class));
            verify(matchMapper, times(1)).toResponsePage(matchPage);
        }

        @Test
        @DisplayName("Should get matches with status filter")
        void shouldGetMatchesWithStatusFilter() throws Exception {
            Match match1 = new Match(1L, 10L, 1L, 2L, 3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            List<Match> matches = Arrays.asList(match1);

            MatchResponseDTO response1 = new MatchResponseDTO(1L, 10L, 1L, 2L, 3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            List<MatchResponseDTO> responses = Arrays.asList(response1);

            Page<Match> matchPage = new Page<>(matches, 0, 20, 1);
            Page<MatchResponseDTO> dtoPage = new Page<>(responses, 0, 20, 1);

            when(getMatchUseCase.getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class)))
                    .thenReturn(matchPage);
            when(matchMapper.toResponsePage(matchPage)).thenReturn(dtoPage);

            mockMvc.perform(get("/tournaments/10/matches")
                    .param("status", "FINISHED")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].status").value("FINISHED"));

            verify(getMatchUseCase, times(1)).getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class));
            verify(matchMapper, times(1)).toResponsePage(matchPage);
        }

        @Test
        @DisplayName("Should get matches with custom pagination")
        void shouldGetMatchesWithCustomPagination() throws Exception {
            Match match1 = new Match(1L, 10L, 1L, 2L, TEST_DATE, "Stadium A");
            List<Match> matches = Arrays.asList(match1);

            MatchResponseDTO response1 = new MatchResponseDTO(1L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.SCHEDULED);
            List<MatchResponseDTO> responses = Arrays.asList(response1);

            Page<Match> matchPage = new Page<>(matches, 1, 10, 15);
            Page<MatchResponseDTO> dtoPage = new Page<>(responses, 1, 10, 15);

            when(getMatchUseCase.getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class)))
                    .thenReturn(matchPage);
            when(matchMapper.toResponsePage(matchPage)).thenReturn(dtoPage);

            mockMvc.perform(get("/tournaments/10/matches")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sortBy", "matchDate")
                    .param("direction", "DESC")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(15))
                .andExpect(jsonPath("$.totalPages").value(2));

            verify(getMatchUseCase, times(1)).getByTournamentIdWithFilters(eq(10L), any(MatchSearchCriteria.class), any(PageRequest.class));
            verify(matchMapper, times(1)).toResponsePage(matchPage);
        }
    }

    @Nested
    @DisplayName("PUT /tournaments/{tournamentId}/matches/{matchId}")
    class UpdateMatchTests {

        @Test
        @DisplayName("Should update match successfully with valid data")
        void shouldUpdateMatchSuccessfully() throws Exception {
            MatchRequestDTO updateRequest = new MatchRequestDTO(1L, 2L, TEST_DATE.plusHours(2), "Stadium B");
            Match updatedMatch = new Match(1L, 10L, 1L, 2L, TEST_DATE.plusHours(2), "Stadium B");
            MatchResponseDTO updatedResponse = new MatchResponseDTO(
                1L, 10L, 1L, 2L, null, null, TEST_DATE.plusHours(2), "Stadium B", MatchStatus.SCHEDULED
            );

            when(matchMapper.toUpdateCommand(eq(1L), any(MatchRequestDTO.class))).thenReturn(updateMatchCommand);
            when(updateMatchUseCase.update(any(UpdateMatchUseCase.UpdateMatchCommand.class))).thenReturn(updatedMatch);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(updatedResponse);

            mockMvc.perform(put("/tournaments/10/matches/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.field").value("Stadium B"));

            verify(matchMapper, times(1)).toUpdateCommand(eq(1L), any(MatchRequestDTO.class));
            verify(updateMatchUseCase, times(1)).update(any(UpdateMatchUseCase.UpdateMatchCommand.class));
            verify(matchMapper, times(1)).toResponse(any(Match.class));
        }

        @Test
        @DisplayName("Should return 400 when update with invalid data")
        void shouldReturn400WhenUpdateWithInvalidData() throws Exception {
            MatchRequestDTO invalidRequest = new MatchRequestDTO(1L, 2L, TEST_DATE, "");

            mockMvc.perform(put("/tournaments/10/matches/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(updateMatchUseCase, never()).update(any(UpdateMatchUseCase.UpdateMatchCommand.class));
        }

        @Test
        @DisplayName("Should invoke use case when updating match")
        void shouldInvokeUseCaseWhenUpdatingMatch() throws Exception {
            when(matchMapper.toUpdateCommand(eq(999L), any(MatchRequestDTO.class))).thenReturn(updateMatchCommand);
            when(updateMatchUseCase.update(any(UpdateMatchUseCase.UpdateMatchCommand.class))).thenReturn(match);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(matchResponseDTO);

            mockMvc.perform(put("/tournaments/10/matches/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(matchRequestDTO)))
                .andExpect(status().isOk());

            verify(matchMapper, times(1)).toUpdateCommand(eq(999L), any(MatchRequestDTO.class));
            verify(updateMatchUseCase, times(1)).update(any(UpdateMatchUseCase.UpdateMatchCommand.class));
        }
    }

    @Nested
    @DisplayName("PUT /tournaments/{tournamentId}/matches/{matchId}/result")
    class SetResultTests {

        @Test
        @DisplayName("Should set match result successfully")
        void shouldSetMatchResultSuccessfully() throws Exception {
            Match finishedMatch = new Match(1L, 10L, 1L, 2L, 3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            MatchResponseDTO finishedResponse = new MatchResponseDTO(
                1L, 10L, 1L, 2L, 3, 1, TEST_DATE, "Stadium A", MatchStatus.FINISHED
            );

            when(matchMapper.toFinishCommand(eq(1L), any(FinishMatchRequestDTO.class))).thenReturn(finishMatchCommand);
            when(finishMatchUseCase.finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class))).thenReturn(finishedMatch);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(finishedResponse);

            mockMvc.perform(put("/tournaments/10/matches/1/result")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(finishMatchRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.homeTeamScore").value(3))
                .andExpect(jsonPath("$.awayTeamScore").value(1))
                .andExpect(jsonPath("$.status").value("FINISHED"));

            verify(matchMapper, times(1)).toFinishCommand(eq(1L), any(FinishMatchRequestDTO.class));
            verify(finishMatchUseCase, times(1)).finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class));
            verify(matchMapper, times(1)).toResponse(any(Match.class));
        }

        @Test
        @DisplayName("Should return 400 when homeTeamScore is null")
        void shouldReturn400WhenHomeTeamScoreIsNull() throws Exception {
            FinishMatchRequestDTO invalidRequest = new FinishMatchRequestDTO(null, 1);

            mockMvc.perform(put("/tournaments/10/matches/1/result")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(finishMatchUseCase, never()).finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when awayTeamScore is null")
        void shouldReturn400WhenAwayTeamScoreIsNull() throws Exception {
            FinishMatchRequestDTO invalidRequest = new FinishMatchRequestDTO(3, null);

            mockMvc.perform(put("/tournaments/10/matches/1/result")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(finishMatchUseCase, never()).finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class));
        }

        @Test
        @DisplayName("Should return 400 when homeTeamScore is negative")
        void shouldReturn400WhenHomeTeamScoreIsNegative() throws Exception {
            FinishMatchRequestDTO invalidRequest = new FinishMatchRequestDTO(-1, 1);

            mockMvc.perform(put("/tournaments/10/matches/1/result")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

            verify(finishMatchUseCase, never()).finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class));
        }

        @Test
        @DisplayName("Should accept zero scores")
        void shouldAcceptZeroScores() throws Exception {
            FinishMatchRequestDTO zeroScoresRequest = new FinishMatchRequestDTO(0, 0);
            Match finishedMatch = new Match(1L, 10L, 1L, 2L, 0, 0, TEST_DATE, "Stadium A", MatchStatus.FINISHED);
            MatchResponseDTO finishedResponse = new MatchResponseDTO(
                1L, 10L, 1L, 2L, 0, 0, TEST_DATE, "Stadium A", MatchStatus.FINISHED
            );

            when(matchMapper.toFinishCommand(eq(1L), any(FinishMatchRequestDTO.class)))
                .thenReturn(new FinishMatchUseCase.FinishMatchCommand(1L, 0, 0));
            when(finishMatchUseCase.finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class))).thenReturn(finishedMatch);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(finishedResponse);

            mockMvc.perform(put("/tournaments/10/matches/1/result")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(zeroScoresRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.homeTeamScore").value(0))
                .andExpect(jsonPath("$.awayTeamScore").value(0));

            verify(finishMatchUseCase, times(1)).finishMatch(any(FinishMatchUseCase.FinishMatchCommand.class));
        }
    }

    @Nested
    @DisplayName("POST /tournaments/{tournamentId}/matches/{matchId}/postpone")
    class PostponeMatchTests {

        @Test
        @DisplayName("Should postpone match successfully")
        void shouldPostponeMatchSuccessfully() throws Exception {
            Match postponedMatch = new Match(1L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.POSTPONED);
            MatchResponseDTO postponedResponse = new MatchResponseDTO(
                1L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.POSTPONED
            );

            when(postponeMatchUseCase.postponeMatch(1L)).thenReturn(postponedMatch);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(postponedResponse);

            mockMvc.perform(post("/tournaments/10/matches/1/postpone")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("POSTPONED"));

            verify(postponeMatchUseCase, times(1)).postponeMatch(1L);
            verify(matchMapper, times(1)).toResponse(any(Match.class));
        }

        @Test
        @DisplayName("Should invoke use case when postponing match")
        void shouldInvokeUseCaseWhenPostponingMatch() throws Exception {
            Match postponedMatch = new Match(999L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.POSTPONED);
            MatchResponseDTO postponedResponse = new MatchResponseDTO(
                999L, 10L, 1L, 2L, null, null, TEST_DATE, "Stadium A", MatchStatus.POSTPONED
            );

            when(postponeMatchUseCase.postponeMatch(999L)).thenReturn(postponedMatch);
            when(matchMapper.toResponse(any(Match.class))).thenReturn(postponedResponse);

            mockMvc.perform(post("/tournaments/10/matches/999/postpone")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            verify(postponeMatchUseCase, times(1)).postponeMatch(999L);
        }
    }

    @Nested
    @DisplayName("DELETE /tournaments/{tournamentId}/matches/{matchId}")
    class DeleteMatchTests {

        @Test
        @DisplayName("Should delete match successfully")
        void shouldDeleteMatchSuccessfully() throws Exception {
            doNothing().when(deleteMatchUseCase).delete(1L);

            mockMvc.perform(delete("/tournaments/10/matches/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(deleteMatchUseCase, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Should invoke use case when deleting match")
        void shouldInvokeUseCaseWhenDeletingMatch() throws Exception {
            doNothing().when(deleteMatchUseCase).delete(999L);

            mockMvc.perform(delete("/tournaments/10/matches/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(deleteMatchUseCase, times(1)).delete(999L);
        }
    }
}
