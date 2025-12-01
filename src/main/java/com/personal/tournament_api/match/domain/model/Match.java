package com.personal.tournament_api.match.domain.model;

import com.personal.tournament_api.match.domain.exceptions.*;

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
    private String field;
    private MatchStatus status;

    public Match(Long id, Long tournamentId, Long homeTeamId, Long awayTeamId,
                 LocalDateTime matchDate, String field) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.matchDate = matchDate;
        this.field = field;
        this.status = MatchStatus.SCHEDULED;
        this.homeTeamScore = null;
        this.awayTeamScore = null;
        validate();
    }

    // Constructor para cargar desde persistencia
    public Match(Long id, Long tournamentId, Long homeTeamId, Long awayTeamId,
                 Integer homeTeamScore, Integer awayTeamScore, LocalDateTime matchDate,
                 String field, MatchStatus status) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.matchDate = matchDate;
        this.field = field;
        this.status = status;
    }

    // --- Domain Rules ---
    public void validate() {
        if (isTournamentIdInvalid()) {
            throw new InvalidMatchTournamentIdException();
        }
        if (areTeamsInvalid()) {
            throw new InvalidMatchTeamsException();
        }
        if (isFieldInvalid()) {
            throw new InvalidMatchFieldException();
        }
        if (isMatchDateInvalid()) {
            throw new InvalidMatchDateException();
        }
    }

    private boolean isTournamentIdInvalid() {
        return tournamentId == null || tournamentId <= 0;
    }

    private boolean areTeamsInvalid() {
        if (homeTeamId == null || homeTeamId <= 0) return true;
        if (awayTeamId == null || awayTeamId <= 0) return true;
        return homeTeamId.equals(awayTeamId);
    }

    private boolean isFieldInvalid() {
        return field == null || field.trim().isEmpty() || field.length() > 100;
    }

    private boolean isMatchDateInvalid() {
        return matchDate == null;
    }

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

    public void updateMatchDetails(LocalDateTime matchDate, String field) {
        if (status == MatchStatus.FINISHED) {
            throw new InvalidMatchStatusTransitionException("Cannot update details of a finished match");
        }
        this.matchDate = matchDate;
        this.field = field;
        validate();
    }

    private void validateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new InvalidMatchScoreException();
        }
    }

    public boolean hasResult() {
        return this.homeTeamScore != null && this.awayTeamScore != null;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public String getField() {
        return field;
    }

    public MatchStatus getStatus() {
        return status;
    }

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
               Objects.equals(field, match.field) &&
               status == match.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tournamentId, homeTeamId, awayTeamId, homeTeamScore,
                          awayTeamScore, matchDate, field, status);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", tournamentId=" + tournamentId +
                ", homeTeamId=" + homeTeamId +
                ", awayTeamId=" + awayTeamId +
                ", homeTeamScore=" + homeTeamScore +
                ", awayTeamScore=" + awayTeamScore +
                ", matchDate=" + matchDate +
                ", field='" + field + '\'' +
                ", status=" + status +
                '}';
    }
}
