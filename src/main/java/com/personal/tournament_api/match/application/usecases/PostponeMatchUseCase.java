package com.personal.tournament_api.match.application.usecases;

import com.personal.tournament_api.match.domain.model.Match;

public interface PostponeMatchUseCase {

    Match postponeMatch(Long matchId);
}
