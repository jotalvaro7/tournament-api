package com.personal.tournament_api.match.domain.model;

import com.personal.tournament_api.match.domain.exceptions.*;
import com.personal.tournament_api.match.domain.model.vo.MatchField;

import java.time.LocalDateTime;
import java.util.Objects;

public class Match {

    private final Long id;
    private final Long tournamentId;
    private final Long homeTeamId;
    private final Long awayTeamId;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private LocalDateTime matchDate;
    private MatchField field;
    private MatchStatus status;
    private Integer matchday;

    private Match(Long id, Long tournamentId, Long homeTeamId, Long awayTeamId,
                  Integer homeTeamScore, Integer awayTeamScore,
                  LocalDateTime matchDate, MatchField field, MatchStatus status, Integer matchday) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.matchDate = matchDate;
        this.field = field;
        this.status = status;
        this.matchday = matchday;
    }

    // --- Factory Methods ---

    public static Match create(Long tournamentId, Long homeTeamId, Long awayTeamId,
                               LocalDateTime matchDate, String field, Integer matchday) {
        validateIdentifiers(tournamentId, homeTeamId, awayTeamId);
        validateMatchDate(matchDate);
        validateMatchday(matchday);
        return new Match(null, tournamentId, homeTeamId, awayTeamId,
                null, null, matchDate, new MatchField(field), MatchStatus.SCHEDULED, matchday);
    }

    public static Match reconstitute(Long id, Long tournamentId, Long homeTeamId, Long awayTeamId,
                                     Integer homeTeamScore, Integer awayTeamScore,
                                     LocalDateTime matchDate, String field, MatchStatus status, Integer matchday) {
        return new Match(id, tournamentId, homeTeamId, awayTeamId,
                homeTeamScore, awayTeamScore, matchDate, new MatchField(field), status, matchday);
    }

    // --- Domain Behavior ---

    public void postponeMatch() {
        if (status == MatchStatus.FINISHED) {
            throw new InvalidMatchStatusTransitionException("Cannot postpone a finished match");
        }
        this.status = MatchStatus.POSTPONED;
    }

    public MatchResultOutcome setMatchResult(int homeScore, int awayScore) {
        if (status != MatchStatus.SCHEDULED && status != MatchStatus.FINISHED) {
            throw new InvalidMatchStatusTransitionException("Only scheduled or finished matches can have their result set");
        }
        validateScore(homeScore, awayScore);

        boolean isCorrection = this.homeTeamScore != null && this.awayTeamScore != null;
        Integer previousHomeScore = this.homeTeamScore;
        Integer previousAwayScore = this.awayTeamScore;

        this.homeTeamScore = homeScore;
        this.awayTeamScore = awayScore;
        this.status = MatchStatus.FINISHED;

        return isCorrection
                ? MatchResultOutcome.correction(previousHomeScore, previousAwayScore)
                : MatchResultOutcome.newResult();
    }

    public void updateMatchDetails(LocalDateTime matchDate, String field, Integer matchday) {
        if (status == MatchStatus.FINISHED) {
            throw new InvalidMatchStatusTransitionException("Cannot update details of a finished match");
        }
        validateMatchDate(matchDate);
        validateMatchday(matchday);
        this.matchDate = matchDate;
        this.field = new MatchField(field);
        this.matchday = matchday;
    }

    public boolean hasResult() {
        return this.homeTeamScore != null && this.awayTeamScore != null;
    }

    // --- Validation Helpers ---

    private static void validateIdentifiers(Long tournamentId, Long homeTeamId, Long awayTeamId) {
        if (tournamentId == null || tournamentId <= 0) throw new InvalidMatchTournamentIdException();
        if (homeTeamId == null || homeTeamId <= 0) throw new InvalidMatchTeamsException();
        if (awayTeamId == null || awayTeamId <= 0) throw new InvalidMatchTeamsException();
        if (homeTeamId.equals(awayTeamId)) throw new InvalidMatchTeamsException();
    }

    private static void validateMatchDate(LocalDateTime matchDate) {
        if (matchDate == null) throw new InvalidMatchDateException();
    }

    private static void validateMatchday(Integer matchday) {
        if (matchday != null && matchday <= 0) {
            throw new InvalidMatchDataException("Matchday must be a positive number");
        }
    }

    private void validateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) throw new InvalidMatchScoreException();
    }

    // --- Getters ---

    public Long getId() { return id; }
    public Long getTournamentId() { return tournamentId; }
    public Long getHomeTeamId() { return homeTeamId; }
    public Long getAwayTeamId() { return awayTeamId; }
    public Integer getHomeTeamScore() { return homeTeamScore; }
    public Integer getAwayTeamScore() { return awayTeamScore; }
    public LocalDateTime getMatchDate() { return matchDate; }
    public String getField() { return field.value(); }
    public MatchStatus getStatus() { return status; }
    public Integer getMatchday() { return matchday; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(id, match.id) &&
               Objects.equals(tournamentId, match.tournamentId) &&
               Objects.equals(homeTeamId, match.homeTeamId) &&
               Objects.equals(awayTeamId, match.awayTeamId) &&
               Objects.equals(homeTeamScore, match.homeTeamScore) &&
               Objects.equals(awayTeamScore, match.awayTeamScore) &&
               Objects.equals(matchDate, match.matchDate) &&
               Objects.equals(getField(), match.getField()) &&
               status == match.status &&
               Objects.equals(matchday, match.matchday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tournamentId, homeTeamId, awayTeamId,
                homeTeamScore, awayTeamScore, matchDate, getField(), status, matchday);
    }

    @Override
    public String toString() {
        return "Match{id=" + id + ", tournamentId=" + tournamentId +
               ", homeTeamId=" + homeTeamId + ", awayTeamId=" + awayTeamId +
               ", homeTeamScore=" + homeTeamScore + ", awayTeamScore=" + awayTeamScore +
               ", matchDate=" + matchDate + ", field='" + getField() + "', status=" + status +
               ", matchday=" + matchday + '}';
    }
}