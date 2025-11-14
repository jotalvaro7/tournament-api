package com.personal.tournament_api.match.domain.model;

public record MatchResultOutcome(
        boolean isCorrection,
        Integer previousHomeScore,
        Integer previousAwayScore
) {
    public static MatchResultOutcome newResult() {
        return new MatchResultOutcome(false, null, null);
    }

    public static MatchResultOutcome correction(Integer previousHomeScore, Integer previousAwayScore) {
        return new MatchResultOutcome(true, previousHomeScore, previousAwayScore);
    }
}
