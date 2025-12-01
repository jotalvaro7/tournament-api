package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.*;
import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchResultOutcome;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import com.personal.tournament_api.match.domain.services.MatchResultService;
import com.personal.tournament_api.team.domain.exceptions.TeamNotFoundException;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.domain.ports.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MatchService Unit Tests")
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchResultService matchResultService;

    @InjectMocks
    private MatchService matchService;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @Nested
    @DisplayName("Create Match Tests")
    class CreateMatchTests {

        @Test
        @DisplayName("Should create match successfully")
        void shouldCreateMatchSuccessfully() {
            // Given
            CreateMatchUseCase.CreateMatchCommand command = new CreateMatchUseCase.CreateMatchCommand(
                TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A"
            );
            Match expectedMatch = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID,
                AWAY_TEAM_ID, VALID_DATE, "Stadium A");

            when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

            // When
            Match result = matchService.create(command);

            // Then
            assertNotNull(result);
            assertEquals(MATCH_ID, result.getId());
            assertEquals(TOURNAMENT_ID, result.getTournamentId());
            assertEquals(HOME_TEAM_ID, result.getHomeTeamId());
            assertEquals(AWAY_TEAM_ID, result.getAwayTeamId());
            assertEquals(MatchStatus.SCHEDULED, result.getStatus());

            verify(matchRepository, times(1)).save(any(Match.class));
        }

        @Test
        @DisplayName("Should save match with correct data")
        void shouldSaveMatchWithCorrectData() {
            // Given
            CreateMatchUseCase.CreateMatchCommand command = new CreateMatchUseCase.CreateMatchCommand(
                TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A"
            );
            Match expectedMatch = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID,
                AWAY_TEAM_ID, VALID_DATE, "Stadium A");

            when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

            // When
            matchService.create(command);

            // Then
            verify(matchRepository).save(argThat(match ->
                match.getTournamentId().equals(TOURNAMENT_ID) &&
                match.getHomeTeamId().equals(HOME_TEAM_ID) &&
                match.getAwayTeamId().equals(AWAY_TEAM_ID) &&
                match.getMatchDate().equals(VALID_DATE) &&
                match.getField().equals("Stadium A") &&
                match.getStatus() == MatchStatus.SCHEDULED
            ));
        }
    }

    @Nested
    @DisplayName("Update Match Tests")
    class UpdateMatchTests {

        @Test
        @DisplayName("Should update match successfully")
        void shouldUpdateMatchSuccessfully() {
            // Given
            Match existingMatch = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID,
                AWAY_TEAM_ID, VALID_DATE, "Stadium A");
            LocalDateTime newDate = VALID_DATE.plusDays(1);
            UpdateMatchUseCase.UpdateMatchCommand command = new UpdateMatchUseCase.UpdateMatchCommand(MATCH_ID, newDate, "Stadium B");

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(existingMatch));
            when(matchRepository.save(any(Match.class))).thenReturn(existingMatch);

            // When
            Match result = matchService.update(command);

            // Then
            assertNotNull(result);
            assertEquals(newDate, result.getMatchDate());
            assertEquals("Stadium B", result.getField());

            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(matchRepository, times(1)).save(any(Match.class));
        }

        @Test
        @DisplayName("Should throw exception when match not found")
        void shouldThrowExceptionWhenMatchNotFound() {
            // Given
            UpdateMatchUseCase.UpdateMatchCommand command = new UpdateMatchUseCase.UpdateMatchCommand(999L, VALID_DATE, "Stadium B");
            when(matchRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(MatchNotFoundException.class, () -> matchService.update(command));
            verify(matchRepository, times(1)).findById(999L);
            verify(matchRepository, never()).save(any(Match.class));
        }
    }

    @Nested
    @DisplayName("Finish Match Tests")
    class FinishMatchTests {

        @Test
        @DisplayName("Should finish match with new result successfully")
        void shouldFinishMatchWithNewResultSuccessfully() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(AWAY_TEAM_ID, "Away Team", "Coach B", TOURNAMENT_ID);
            FinishMatchUseCase.FinishMatchCommand command = new FinishMatchUseCase.FinishMatchCommand(MATCH_ID, 3, 1);
            MatchResultOutcome outcome = MatchResultOutcome.newResult();

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
            when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
            when(matchResultService.registerResult(eq(match), eq(homeTeam), eq(awayTeam),
                eq(3), eq(1))).thenReturn(outcome);
            when(matchRepository.save(any(Match.class))).thenReturn(match);
            when(teamRepository.save(any(Team.class))).thenReturn(homeTeam);

            // When
            Match result = matchService.finishMatch(command);

            // Then
            assertNotNull(result);
            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(teamRepository, times(1)).findById(HOME_TEAM_ID);
            verify(teamRepository, times(1)).findById(AWAY_TEAM_ID);
            verify(matchResultService, times(1)).registerResult(match, homeTeam, awayTeam, 3, 1);
            verify(matchRepository, times(1)).save(match);
            verify(teamRepository, times(2)).save(any(Team.class));
        }

        @Test
        @DisplayName("Should finish match with correction successfully")
        void shouldFinishMatchWithCorrectionSuccessfully() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                2, 2, VALID_DATE, "Stadium A", MatchStatus.FINISHED);
            Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID,
                1, 1, 0, 1, 0, 2, 2, 0);
            Team awayTeam = new Team(AWAY_TEAM_ID, "Away Team", "Coach B", TOURNAMENT_ID,
                1, 1, 0, 1, 0, 2, 2, 0);
            FinishMatchUseCase.FinishMatchCommand command = new FinishMatchUseCase.FinishMatchCommand(MATCH_ID, 3, 1);
            MatchResultOutcome outcome = MatchResultOutcome.correction(2, 2);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
            when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
            when(matchResultService.registerResult(eq(match), eq(homeTeam), eq(awayTeam),
                eq(3), eq(1))).thenReturn(outcome);
            when(matchRepository.save(any(Match.class))).thenReturn(match);
            when(teamRepository.save(any(Team.class))).thenReturn(homeTeam);

            // When
            Match result = matchService.finishMatch(command);

            // Then
            assertNotNull(result);
            verify(matchResultService, times(1)).registerResult(match, homeTeam, awayTeam, 3, 1);
        }

        @Test
        @DisplayName("Should throw exception when match not found")
        void shouldThrowExceptionWhenMatchNotFound() {
            // Given
            FinishMatchUseCase.FinishMatchCommand command = new FinishMatchUseCase.FinishMatchCommand(999L, 3, 1);
            when(matchRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(MatchNotFoundException.class, () -> matchService.finishMatch(command));
            verify(matchRepository, times(1)).findById(999L);
            verify(matchResultService, never()).registerResult(any(), any(), any(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should throw exception when home team not found")
        void shouldThrowExceptionWhenHomeTeamNotFound() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            FinishMatchUseCase.FinishMatchCommand command = new FinishMatchUseCase.FinishMatchCommand(MATCH_ID, 3, 1);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TeamNotFoundException.class, () -> matchService.finishMatch(command));
            verify(teamRepository, times(1)).findById(HOME_TEAM_ID);
            verify(matchResultService, never()).registerResult(any(), any(), any(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("Should throw exception when away team not found")
        void shouldThrowExceptionWhenAwayTeamNotFound() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID);
            FinishMatchUseCase.FinishMatchCommand command = new FinishMatchUseCase.FinishMatchCommand(MATCH_ID, 3, 1);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
            when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TeamNotFoundException.class, () -> matchService.finishMatch(command));
            verify(teamRepository, times(1)).findById(AWAY_TEAM_ID);
            verify(matchResultService, never()).registerResult(any(), any(), any(), anyInt(), anyInt());
        }
    }

    @Nested
    @DisplayName("Postpone Match Tests")
    class PostponeMatchTests {

        @Test
        @DisplayName("Should postpone match successfully")
        void shouldPostponeMatchSuccessfully() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
            when(matchRepository.save(any(Match.class))).thenReturn(match);

            // When
            Match result = matchService.postponeMatch(MATCH_ID);

            // Then
            assertNotNull(result);
            assertEquals(MatchStatus.POSTPONED, result.getStatus());

            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(matchRepository, times(1)).save(match);
        }

        @Test
        @DisplayName("Should throw exception when match not found")
        void shouldThrowExceptionWhenMatchNotFound() {
            // Given
            when(matchRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(MatchNotFoundException.class, () -> matchService.postponeMatch(999L));
            verify(matchRepository, times(1)).findById(999L);
            verify(matchRepository, never()).save(any(Match.class));
        }
    }

    @Nested
    @DisplayName("Get Match Tests")
    class GetMatchTests {

        @Test
        @DisplayName("Should get match by id successfully")
        void shouldGetMatchByIdSuccessfully() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));

            // When
            Optional<Match> result = matchService.getById(MATCH_ID);

            // Then
            assertTrue(result.isPresent());
            assertEquals(MATCH_ID, result.get().getId());
            verify(matchRepository, times(1)).findById(MATCH_ID);
        }

        @Test
        @DisplayName("Should return empty when match not found")
        void shouldReturnEmptyWhenMatchNotFound() {
            // Given
            when(matchRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<Match> result = matchService.getById(999L);

            // Then
            assertFalse(result.isPresent());
            verify(matchRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Should get all matches by tournament id")
        void shouldGetAllMatchesByTournamentId() {
            // Given
            Match match1 = new Match(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            Match match2 = new Match(2L, TOURNAMENT_ID, 3L, 4L,
                VALID_DATE.plusDays(1), "Stadium B");
            List<Match> matches = Arrays.asList(match1, match2);

            when(matchRepository.findAllByTournamentId(TOURNAMENT_ID)).thenReturn(matches);

            // When
            List<Match> result = matchService.getAllByTournamentId(TOURNAMENT_ID);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(m -> m.getTournamentId().equals(TOURNAMENT_ID)));
            verify(matchRepository, times(1)).findAllByTournamentId(TOURNAMENT_ID);
        }

        @Test
        @DisplayName("Should return empty list when no matches for tournament")
        void shouldReturnEmptyListWhenNoMatchesForTournament() {
            // Given
            when(matchRepository.findAllByTournamentId(TOURNAMENT_ID)).thenReturn(List.of());

            // When
            List<Match> result = matchService.getAllByTournamentId(TOURNAMENT_ID);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(matchRepository, times(1)).findAllByTournamentId(TOURNAMENT_ID);
        }

        @Test
        @DisplayName("Should get all matches by team id")
        void shouldGetAllMatchesByTeamId() {
            // Given
            Match match1 = new Match(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            Match match2 = new Match(2L, TOURNAMENT_ID, HOME_TEAM_ID, 3L,
                VALID_DATE.plusDays(1), "Stadium B");
            List<Match> matches = Arrays.asList(match1, match2);

            when(matchRepository.findAllByTeamId(HOME_TEAM_ID)).thenReturn(matches);

            // When
            List<Match> result = matchService.getAllByTeamId(HOME_TEAM_ID);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(matchRepository, times(1)).findAllByTeamId(HOME_TEAM_ID);
        }

        @Test
        @DisplayName("Should return empty list when no matches for team")
        void shouldReturnEmptyListWhenNoMatchesForTeam() {
            // Given
            when(matchRepository.findAllByTeamId(HOME_TEAM_ID)).thenReturn(List.of());

            // When
            List<Match> result = matchService.getAllByTeamId(HOME_TEAM_ID);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(matchRepository, times(1)).findAllByTeamId(HOME_TEAM_ID);
        }
    }

    @Nested
    @DisplayName("Delete Match Tests")
    class DeleteMatchTests {

        @Test
        @DisplayName("Should delete match without result successfully")
        void shouldDeleteMatchWithoutResultSuccessfully() {
            // Given
            Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                VALID_DATE, "Stadium A");
            Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID);
            Team awayTeam = new Team(AWAY_TEAM_ID, "Away Team", "Coach B", TOURNAMENT_ID);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
            when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
            doNothing().when(matchResultService).prepareMatchForDeletion(match, homeTeam, awayTeam);
            when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(matchRepository).deleteById(MATCH_ID);

            // When
            matchService.delete(MATCH_ID);

            // Then
            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(teamRepository, times(1)).findById(HOME_TEAM_ID);
            verify(teamRepository, times(1)).findById(AWAY_TEAM_ID);
            verify(matchResultService, times(1)).prepareMatchForDeletion(match, homeTeam, awayTeam);
            verify(teamRepository, times(2)).save(any(Team.class));
            verify(matchRepository, times(1)).deleteById(MATCH_ID);
        }

        @Test
        @DisplayName("Should delete match with result and revert statistics")
        void shouldDeleteMatchWithResultAndRevertStatistics() {
            // Given
            Match matchWithResult = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                3, 1, VALID_DATE, "Stadium A", MatchStatus.FINISHED);
            Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID,
                3, 1, 1, 0, 0, 3, 1, 2);
            Team awayTeam = new Team(AWAY_TEAM_ID, "Away Team", "Coach B", TOURNAMENT_ID,
                0, 1, 0, 0, 1, 1, 3, -2);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(matchWithResult));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
            when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
            doNothing().when(matchResultService).prepareMatchForDeletion(matchWithResult, homeTeam, awayTeam);
            when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));
            doNothing().when(matchRepository).deleteById(MATCH_ID);

            // When
            matchService.delete(MATCH_ID);

            // Then
            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(teamRepository, times(1)).findById(HOME_TEAM_ID);
            verify(teamRepository, times(1)).findById(AWAY_TEAM_ID);
            verify(matchResultService, times(1)).prepareMatchForDeletion(matchWithResult, homeTeam, awayTeam);
            verify(teamRepository, times(2)).save(any(Team.class));
            verify(matchRepository, times(1)).deleteById(MATCH_ID);
        }

        @Test
        @DisplayName("Should throw exception when match not found")
        void shouldThrowExceptionWhenMatchNotFound() {
            // Given
            when(matchRepository.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(MatchNotFoundException.class, () -> matchService.delete(999L));
            verify(matchRepository, times(1)).findById(999L);
            verify(matchRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw exception when home team not found during delete")
        void shouldThrowExceptionWhenHomeTeamNotFoundDuringDelete() {
            // Given
            Match matchWithResult = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                3, 1, VALID_DATE, "Stadium A", MatchStatus.FINISHED);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(matchWithResult));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TeamNotFoundException.class, () -> matchService.delete(MATCH_ID));
            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(teamRepository, times(1)).findById(HOME_TEAM_ID);
            verify(matchResultService, never()).prepareMatchForDeletion(any(), any(), any());
            verify(matchRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("Should throw exception when away team not found during delete")
        void shouldThrowExceptionWhenAwayTeamNotFoundDuringDelete() {
            // Given
            Match matchWithResult = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                3, 1, VALID_DATE, "Stadium A", MatchStatus.FINISHED);
            Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID,
                3, 1, 1, 0, 0, 3, 1, 2);

            when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(matchWithResult));
            when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
            when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.empty());

            // When & Then
            assertThrows(TeamNotFoundException.class, () -> matchService.delete(MATCH_ID));
            verify(matchRepository, times(1)).findById(MATCH_ID);
            verify(teamRepository, times(1)).findById(HOME_TEAM_ID);
            verify(teamRepository, times(1)).findById(AWAY_TEAM_ID);
            verify(matchResultService, never()).prepareMatchForDeletion(any(), any(), any());
            verify(matchRepository, never()).deleteById(anyLong());
        }
    }
}
