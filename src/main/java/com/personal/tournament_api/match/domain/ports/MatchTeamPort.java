package com.personal.tournament_api.match.domain.ports;

public interface MatchTeamPort {

    void validateBelongsToTournament(Long teamId, Long tournamentId);

    void recordMatchResult(Long teamId, int goalsFor, int goalsAgainst);

    void reverseMatchResult(Long teamId, int goalsFor, int goalsAgainst);
}
