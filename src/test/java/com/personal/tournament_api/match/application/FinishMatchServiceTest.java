package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase.FinishMatchCommand;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("FinishMatchService Tests")
@ExtendWith(MockitoExtension.class)
class FinishMatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private MatchResultService matchResultService;

    private FinishMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new FinishMatchService(matchRepository, teamRepository, matchResultService);
    }

    @Test
    @DisplayName("Should finish match with new result successfully")
    void shouldFinishMatchWithNewResultSuccessfully() {
        // Given
        Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A");
        Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID);
        Team awayTeam = new Team(AWAY_TEAM_ID, "Away Team", "Coach B", TOURNAMENT_ID);
        FinishMatchCommand command = new FinishMatchCommand(MATCH_ID, 3, 1);
        MatchResultOutcome outcome = MatchResultOutcome.newResult();

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
        when(matchResultService.registerResult(eq(match), eq(homeTeam), eq(awayTeam), eq(3), eq(1))).thenReturn(outcome);
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(teamRepository.save(any(Team.class))).thenReturn(homeTeam);

        // When
        Match result = service.finishMatch(command);

        // Then
        assertNotNull(result);
        verify(matchResultService).registerResult(match, homeTeam, awayTeam, 3, 1);
        verify(matchRepository).save(match);
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
        FinishMatchCommand command = new FinishMatchCommand(MATCH_ID, 3, 1);
        MatchResultOutcome outcome = MatchResultOutcome.correction(2, 2);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
        when(matchResultService.registerResult(eq(match), eq(homeTeam), eq(awayTeam), eq(3), eq(1))).thenReturn(outcome);
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(teamRepository.save(any(Team.class))).thenReturn(homeTeam);

        // When
        Match result = service.finishMatch(command);

        // Then
        assertNotNull(result);
        verify(matchResultService).registerResult(match, homeTeam, awayTeam, 3, 1);
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match not found")
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        FinishMatchCommand command = new FinishMatchCommand(999L, 3, 1);
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MatchNotFoundException.class, () -> service.finishMatch(command));
        verify(matchResultService, never()).registerResult(any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Should throw TeamNotFoundException when home team not found")
    void shouldThrowExceptionWhenHomeTeamNotFound() {
        // Given
        Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A");
        FinishMatchCommand command = new FinishMatchCommand(MATCH_ID, 3, 1);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> service.finishMatch(command));
        verify(matchResultService, never()).registerResult(any(), any(), any(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Should throw TeamNotFoundException when away team not found")
    void shouldThrowExceptionWhenAwayTeamNotFound() {
        // Given
        Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A");
        Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID);
        FinishMatchCommand command = new FinishMatchCommand(MATCH_ID, 3, 1);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> service.finishMatch(command));
        verify(matchResultService, never()).registerResult(any(), any(), any(), anyInt(), anyInt());
    }
}
