package com.personal.tournament_api.match.application.usecases;

import com.personal.tournament_api.match.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface GetMatchUseCase {

    Optional<Match> getById(Long matchId);

    List<Match> getAllByTournamentId(Long tournamentId);

    List<Match> getAllByTeamId(Long teamId);
}
