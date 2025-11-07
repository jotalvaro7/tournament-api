package com.personal.tournament_api.match.domain.ports;

import com.personal.tournament_api.match.domain.model.Match;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {

    Match save(Match match);

    Optional<Match> findById(Long id);

    List<Match> findAllByTournamentId(Long tournamentId);

    List<Match> findAllByTeamId(Long teamId);

    void deleteById(Long id);

    void deleteByTournamentId(Long tournamentId);
}
