package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.domain.exceptions.MatchNotFoundException;
import com.personal.tournament_api.match.domain.model.Match;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("DeleteMatchService Tests")
@ExtendWith(MockitoExtension.class)
class DeleteMatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private MatchResultService matchResultService;

    private DeleteMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long MATCH_ID = 1L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new DeleteMatchService(matchRepository, teamRepository, matchResultService);
    }

    @Test
    @DisplayName("Should delete match without result successfully")
    void shouldDeleteMatchWithoutResultSuccessfully() {
        // Given
        Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A");
        Team homeTeam = new Team(HOME_TEAM_ID, "Home Team", "Coach A", TOURNAMENT_ID);
        Team awayTeam = new Team(AWAY_TEAM_ID, "Away Team", "Coach B", TOURNAMENT_ID);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findById(AWAY_TEAM_ID)).thenReturn(Optional.of(awayTeam));
        doNothing().when(matchResultService).prepareMatchForDeletion(match, homeTeam, awayTeam);
        when(teamRepository.save(any(Team.class))).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(matchRepository).deleteById(MATCH_ID);

        // When
        service.delete(MATCH_ID);

        // Then
        verify(matchResultService).prepareMatchForDeletion(match, homeTeam, awayTeam);
        verify(teamRepository, times(2)).save(any(Team.class));
        verify(matchRepository).deleteById(MATCH_ID);
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
        when(teamRepository.save(any(Team.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        service.delete(MATCH_ID);

        // Then
        verify(matchResultService).prepareMatchForDeletion(matchWithResult, homeTeam, awayTeam);
        verify(teamRepository, times(2)).save(any(Team.class));
        verify(matchRepository).deleteById(MATCH_ID);
    }

    @Test
    @DisplayName("Should throw MatchNotFoundException when match not found")
    void shouldThrowExceptionWhenMatchNotFound() {
        // Given
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MatchNotFoundException.class, () -> service.delete(999L));
        verify(matchRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw TeamNotFoundException when home team not found")
    void shouldThrowExceptionWhenHomeTeamNotFound() {
        // Given
        Match match = new Match(MATCH_ID, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID,
                3, 1, VALID_DATE, "Stadium A", MatchStatus.FINISHED);

        when(matchRepository.findById(MATCH_ID)).thenReturn(Optional.of(match));
        when(teamRepository.findById(HOME_TEAM_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TeamNotFoundException.class, () -> service.delete(MATCH_ID));
        verify(matchResultService, never()).prepareMatchForDeletion(any(), any(), any());
        verify(matchRepository, never()).deleteById(anyLong());
    }
}
