package com.personal.tournament_api.match.domain.services;

import com.personal.tournament_api.match.domain.exceptions.InvalidMatchDataException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchResultOutcome;
import com.personal.tournament_api.match.domain.ports.MatchTeamPort;

public class MatchResultService {

    public MatchResultOutcome registerResult(Match match, MatchTeamPort teamPort, int homeScore, int awayScore) {
        teamPort.validateBelongsToTournament(match.getHomeTeamId(), match.getTournamentId());
        teamPort.validateBelongsToTournament(match.getAwayTeamId(), match.getTournamentId());

        MatchResultOutcome outcome = match.setMatchResult(homeScore, awayScore);

        if (outcome.isCorrection()) {
            teamPort.reverseMatchResult(match.getHomeTeamId(), outcome.previousHomeScore(), outcome.previousAwayScore());
            teamPort.reverseMatchResult(match.getAwayTeamId(), outcome.previousAwayScore(), outcome.previousHomeScore());
        }

        teamPort.recordMatchResult(match.getHomeTeamId(), homeScore, awayScore);
        teamPort.recordMatchResult(match.getAwayTeamId(), awayScore, homeScore);

        return outcome;
    }

    public void revertMatchResult(Match match, MatchTeamPort teamPort) {
        if (!match.hasResult()) {
            throw new InvalidMatchDataException("Cannot revert result from a match without a result");
        }

        teamPort.validateBelongsToTournament(match.getHomeTeamId(), match.getTournamentId());
        teamPort.validateBelongsToTournament(match.getAwayTeamId(), match.getTournamentId());

        teamPort.reverseMatchResult(match.getHomeTeamId(), match.getHomeTeamScore(), match.getAwayTeamScore());
        teamPort.reverseMatchResult(match.getAwayTeamId(), match.getAwayTeamScore(), match.getHomeTeamScore());
    }

    public void prepareMatchForDeletion(Match match, MatchTeamPort teamPort) {
        if (!match.hasResult()) {
            return;
        }

        teamPort.validateBelongsToTournament(match.getHomeTeamId(), match.getTournamentId());
        teamPort.validateBelongsToTournament(match.getAwayTeamId(), match.getTournamentId());

        teamPort.reverseMatchResult(match.getHomeTeamId(), match.getHomeTeamScore(), match.getAwayTeamScore());
        teamPort.reverseMatchResult(match.getAwayTeamId(), match.getAwayTeamScore(), match.getHomeTeamScore());
    }
}
