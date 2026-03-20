package com.personal.tournament_api.team.domain.model;

import com.personal.tournament_api.team.domain.exceptions.*;
import com.personal.tournament_api.team.domain.model.vo.CoachName;
import com.personal.tournament_api.team.domain.model.vo.TeamName;

import java.util.Objects;

public class Team {

    private final Long id;
    private TeamName name;
    private CoachName coach;
    private final Long tournamentId;
    private int points;
    private int matchesPlayed;
    private int matchesWin;
    private int matchesDraw;
    private int matchesLost;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;

    private Team(Long id, TeamName name, CoachName coach, Long tournamentId,
                 int points, int matchesPlayed, int matchesWin, int matchesDraw,
                 int matchesLost, int goalsFor, int goalsAgainst, int goalDifference) {
        this.id = id;
        this.name = name;
        this.coach = coach;
        this.tournamentId = tournamentId;
        this.points = points;
        this.matchesPlayed = matchesPlayed;
        this.matchesWin = matchesWin;
        this.matchesDraw = matchesDraw;
        this.matchesLost = matchesLost;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDifference = goalDifference;
    }

    // --- Factory Methods ---

    public static Team create(String name, String coach, Long tournamentId) {
        validateTournamentId(tournamentId);
        return new Team(null, new TeamName(name), new CoachName(coach), tournamentId,
                0, 0, 0, 0, 0, 0, 0, 0);
    }

    public static Team reconstitute(Long id, String name, String coach, Long tournamentId,
                                    int points, int matchesPlayed, int matchesWin, int matchesDraw,
                                    int matchesLost, int goalsFor, int goalsAgainst, int goalDifference) {
        return new Team(id, new TeamName(name), new CoachName(coach), tournamentId,
                points, matchesPlayed, matchesWin, matchesDraw,
                matchesLost, goalsFor, goalsAgainst, goalDifference);
    }

    // --- Domain Behavior ---

    public void updateDetails(String name, String coach) {
        this.name = new TeamName(name);
        this.coach = new CoachName(coach);
    }

    public void validateCanBeDeleted(int associatedMatchesCount) {
        if (associatedMatchesCount > 0) {
            throw new TeamHasMatchesException(this.id, associatedMatchesCount);
        }
    }

    public void ensureBelongsToTournament(Long tournamentId) {
        if (!this.tournamentId.equals(tournamentId)) {
            throw new TeamNotInTournamentException(this.id, tournamentId);
        }
    }

    public void recordMatchResult(int goalsFor, int goalsAgainst) {
        validateGoals(goalsFor, goalsAgainst);
        if (goalsFor > goalsAgainst) {
            registerVictory(goalsFor, goalsAgainst);
        } else if (goalsFor == goalsAgainst) {
            registerDraw(goalsFor, goalsAgainst);
        } else {
            registerDefeat(goalsFor, goalsAgainst);
        }
        validateConsistency();
    }

    public void reverseMatchResult(int goalsFor, int goalsAgainst) {
        validateGoals(goalsFor, goalsAgainst);
        if (goalsFor > goalsAgainst) {
            reverseVictory(goalsFor, goalsAgainst);
        } else if (goalsFor == goalsAgainst) {
            reverseDraw(goalsFor, goalsAgainst);
        } else {
            reverseDefeat(goalsFor, goalsAgainst);
        }
    }

    public void registerVictory(int goalsFor, int goalsAgainst) {
        validateGoals(goalsFor, goalsAgainst);
        applyMatchStats(goalsFor, goalsAgainst);
        this.matchesWin++;
        this.points += 3;
    }

    public void registerDraw(int goalsFor, int goalsAgainst) {
        validateGoals(goalsFor, goalsAgainst);
        applyMatchStats(goalsFor, goalsAgainst);
        this.matchesDraw++;
        this.points += 1;
    }

    public void registerDefeat(int goalsFor, int goalsAgainst) {
        validateGoals(goalsFor, goalsAgainst);
        applyMatchStats(goalsFor, goalsAgainst);
        this.matchesLost++;
    }

    private void reverseVictory(int goalsFor, int goalsAgainst) {
        reverseMatchStats(goalsFor, goalsAgainst);
        this.matchesWin--;
        this.points -= 3;
    }

    private void reverseDraw(int goalsFor, int goalsAgainst) {
        reverseMatchStats(goalsFor, goalsAgainst);
        this.matchesDraw--;
        this.points -= 1;
    }

    private void reverseDefeat(int goalsFor, int goalsAgainst) {
        reverseMatchStats(goalsFor, goalsAgainst);
        this.matchesLost--;
    }

    private void validateGoals(int goalsFor, int goalsAgainst) {
        if (goalsFor < 0 || goalsAgainst < 0) {
            throw new InvalidTeamGoalsException();
        }
    }

    private void validateConsistency() {
        if (this.matchesPlayed != (this.matchesWin + this.matchesDraw + this.matchesLost)) {
            throw new TeamMatchesPlayedException();
        }
        if (this.goalDifference != (this.goalsFor - this.goalsAgainst)) {
            throw new TeamGoalsDifferenceException();
        }
    }

    private void applyMatchStats(int goalsFor, int goalsAgainst) {
        this.goalsFor += goalsFor;
        this.goalsAgainst += goalsAgainst;
        this.goalDifference = this.goalsFor - this.goalsAgainst;
        this.matchesPlayed++;
    }

    private void reverseMatchStats(int goalsFor, int goalsAgainst) {
        this.goalsFor -= goalsFor;
        this.goalsAgainst -= goalsAgainst;
        this.goalDifference = this.goalsFor - this.goalsAgainst;
        this.matchesPlayed--;
    }

    private static void validateTournamentId(Long tournamentId) {
        if (tournamentId == null || tournamentId <= 0) {
            throw new InvalidTeamTournamentIdException();
        }
    }

    // --- Getters ---

    public Long getId() { return id; }
    public String getName() { return name.value(); }
    public String getCoach() { return coach.value(); }
    public Long getTournamentId() { return tournamentId; }
    public int getPoints() { return points; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public int getMatchesWin() { return matchesWin; }
    public int getMatchesDraw() { return matchesDraw; }
    public int getMatchesLost() { return matchesLost; }
    public int getGoalsFor() { return goalsFor; }
    public int getGoalsAgainst() { return goalsAgainst; }
    public int getGoalDifference() { return goalDifference; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return points == team.points && matchesPlayed == team.matchesPlayed &&
               matchesWin == team.matchesWin && matchesDraw == team.matchesDraw &&
               matchesLost == team.matchesLost && goalsFor == team.goalsFor &&
               goalsAgainst == team.goalsAgainst && goalDifference == team.goalDifference &&
               Objects.equals(id, team.id) && Objects.equals(getName(), team.getName()) &&
               Objects.equals(getCoach(), team.getCoach()) && Objects.equals(tournamentId, team.tournamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getCoach(), tournamentId, points, matchesPlayed,
                matchesWin, matchesDraw, matchesLost, goalsFor, goalsAgainst, goalDifference);
    }

    @Override
    public String toString() {
        return "Team{id=" + id + ", name='" + getName() + "', coach='" + getCoach() +
               "', tournamentId=" + tournamentId + ", points=" + points + ", matchesPlayed=" + matchesPlayed + '}';
    }
}