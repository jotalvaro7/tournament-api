package com.personal.tournament_api.match.domain.services;

import com.personal.tournament_api.match.domain.exceptions.InvalidMatchDataException;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.MatchResultOutcome;
import com.personal.tournament_api.team.domain.model.Team;

public class MatchResultService {

    public MatchResultOutcome registerResult(
            Match match,
            Team homeTeam,
            Team awayTeam,
            int homeScore,
            int awayScore) {

        validateTeamsFromMatch(match, homeTeam, awayTeam);

        MatchResultOutcome outcome = match.setMatchResult(homeScore, awayScore);

        updateTeamsStatistics(outcome, homeTeam, awayTeam, homeScore, awayScore);

        return outcome;
    }

    public void revertMatchResult(Match match, Team homeTeam, Team awayTeam) {
        if (!match.hasResult()) {
            throw new InvalidMatchDataException("Cannot revert result from a match without a result");
        }

        validateTeamsFromMatch(match, homeTeam, awayTeam);

        homeTeam.reverseMatchResult(match.getHomeTeamScore(), match.getAwayTeamScore());
        awayTeam.reverseMatchResult(match.getAwayTeamScore(), match.getHomeTeamScore());
    }

    public void prepareMatchForDeletion(Match match, Team homeTeam, Team awayTeam) {
        if (!match.hasResult()) {
            return;
        }

        validateTeamsFromMatch(match, homeTeam, awayTeam);

        homeTeam.reverseMatchResult(match.getHomeTeamScore(), match.getAwayTeamScore());
        awayTeam.reverseMatchResult(match.getAwayTeamScore(), match.getHomeTeamScore());
    }

    private void validateTeamsFromMatch(Match match, Team homeTeam, Team awayTeam) {
        validateTeamBelongsToTournament(match, homeTeam, "Home");
        validateTeamBelongsToTournament(match, awayTeam, "Away");
    }

    private void validateTeamBelongsToTournament(Match match, Team team, String teamType) {
        if (!team.getTournamentId().equals(match.getTournamentId())) {
            throw new InvalidMatchDataException(
                String.format("%s team %d does not belong to tournament %d",
                    teamType, team.getId(), match.getTournamentId())
            );
        }
    }

    private void updateTeamsStatistics(MatchResultOutcome outcome, Team homeTeam, Team awayTeam, int homeScore, int awayScore) {

        if (outcome.isCorrection()) {
            reverseTeamsStatistics(outcome, homeTeam, awayTeam);
        }

        applyTeamsStatistics(homeTeam, awayTeam, homeScore, awayScore);
    }

    private void reverseTeamsStatistics(MatchResultOutcome outcome, Team homeTeam, Team awayTeam) {
        homeTeam.reverseMatchResult(outcome.previousHomeScore(), outcome.previousAwayScore());
        awayTeam.reverseMatchResult(outcome.previousAwayScore(), outcome.previousHomeScore());
    }

    private void applyTeamsStatistics(Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
        homeTeam.recordMatchResult(homeScore, awayScore);
        awayTeam.recordMatchResult(awayScore, homeScore);
    }
}
