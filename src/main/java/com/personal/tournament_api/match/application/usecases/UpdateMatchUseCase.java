package com.personal.tournament_api.match.application.usecases;

import com.personal.tournament_api.match.domain.model.Match;

import java.time.LocalDateTime;

public interface UpdateMatchUseCase {

    Match update(UpdateMatchCommand command);

    record UpdateMatchCommand(
            Long matchId,
            LocalDateTime matchDate,
            String field
    ) {}
}
