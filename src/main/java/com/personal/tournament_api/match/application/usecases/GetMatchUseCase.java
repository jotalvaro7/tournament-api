package com.personal.tournament_api.match.application.usecases;

import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchSearchCriteria;
import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.domain.model.PageRequest;

import java.util.List;
import java.util.Optional;

public interface GetMatchUseCase {

    Optional<Match> getById(Long matchId);

    List<Match> getAllByTournamentId(Long tournamentId);

    List<Match> getAllByTeamId(Long teamId);

    Page<Match> getByTournamentIdWithFilters(Long tournamentId, MatchSearchCriteria criteria, PageRequest pageRequest);
}
