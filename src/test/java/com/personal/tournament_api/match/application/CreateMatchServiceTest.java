package com.personal.tournament_api.match.application;

import com.personal.tournament_api.match.application.usecases.CreateMatchUseCase.CreateMatchCommand;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchStatus;
import com.personal.tournament_api.match.domain.ports.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateMatchService Tests")
@ExtendWith(MockitoExtension.class)
class CreateMatchServiceTest {

    @Mock private MatchRepository matchRepository;

    private CreateMatchService service;

    private static final Long TOURNAMENT_ID = 10L;
    private static final Long HOME_TEAM_ID = 1L;
    private static final Long AWAY_TEAM_ID = 2L;
    private static final LocalDateTime VALID_DATE = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        service = new CreateMatchService(matchRepository);
    }

    @Test
    @DisplayName("Should create match successfully with SCHEDULED status")
    void shouldCreateMatchSuccessfully() {
        // Given
        CreateMatchCommand command = new CreateMatchCommand(TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A", null);
        Match expected = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

        when(matchRepository.save(any())).thenReturn(expected);

        // When
        Match result = service.create(command);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(MatchStatus.SCHEDULED, result.getStatus());
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    @DisplayName("Should save match with correct data")
    void shouldSaveMatchWithCorrectData() {
        // Given
        CreateMatchCommand command = new CreateMatchCommand(TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, VALID_DATE, "Stadium A", null);
        Match expected = Match.reconstitute(1L, TOURNAMENT_ID, HOME_TEAM_ID, AWAY_TEAM_ID, null, null, VALID_DATE, "Stadium A", MatchStatus.SCHEDULED, null);

        when(matchRepository.save(any())).thenReturn(expected);

        // When
        service.create(command);

        // Then
        verify(matchRepository).save(argThat(match ->
                match.getTournamentId().equals(TOURNAMENT_ID) &&
                match.getHomeTeamId().equals(HOME_TEAM_ID) &&
                match.getAwayTeamId().equals(AWAY_TEAM_ID) &&
                match.getField().equals("Stadium A") &&
                match.getStatus() == MatchStatus.SCHEDULED
        ));
    }
}
