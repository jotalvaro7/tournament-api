package com.personal.tournament_api.match.application.usecases;

import com.personal.tournament_api.match.domain.model.Match;

public interface FinishMatchUseCase {

    Match finishMatch(FinishMatchCommand command);

    record FinishMatchCommand(
            Long matchId,
            Integer homeTeamScore,
            Integer awayTeamScore
    ) {}
}
