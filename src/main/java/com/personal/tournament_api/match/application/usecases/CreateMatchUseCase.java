package com.personal.tournament_api.match.application.usecases;

import com.personal.tournament_api.match.domain.model.Match;

import java.time.LocalDateTime;

public interface CreateMatchUseCase {

    Match create(CreateMatchCommand command);

    record CreateMatchCommand(
            Long tournamentId,
            Long homeTeamId,
            Long awayTeamId,
            LocalDateTime matchDate,
            String field
    ) {}
}
