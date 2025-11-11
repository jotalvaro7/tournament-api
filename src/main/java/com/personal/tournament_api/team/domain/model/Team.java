package com.personal.tournament_api.team.domain.model;

import com.personal.tournament_api.team.domain.exceptions.*;

import java.util.Objects;

public class Team {
    private final Long id;
    private String name;
    private String coach;
    private final Long tournamentId;
    private int points;
    private int matchesPlayed;
    private int matchesWin;
    private int matchesDraw;
    private int matchesLost;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;

    // Constructor for create a new Team (with stats at 0)
    public Team(Long id, String name, String coach, Long tournamentId) {
        this.id = id;
        this.name = name;
        this.coach = coach;
        this.tournamentId = tournamentId;
        this.points = 0;
        this.matchesPlayed = 0;
        this.matchesWin = 0;
        this.matchesDraw = 0;
        this.matchesLost = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.goalDifference = 0;
        validate();
    }

    // Constructor to REBUILD from persistence (with all statistics)
    public Team(Long id, String name, String coach, Long tournamentId,
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

    // --- Domain Rules ---
    public void validate() {
        if (isNameInvalid()) {
            throw new InvalidTeamNameException();
        }
        if (isCoachInvalid()) {
            throw new InvalidTeamCoachException();
        }
        if (isTournamentIdInvalid()) {
            throw new InvalidTeamTournamentIdException();
        }

    }

    private boolean isNameInvalid() {
        return name == null || name.trim().isEmpty() || name.length() > 100 || name.length() < 3;
    }

    private boolean isCoachInvalid() {
        return coach == null || coach.trim().isEmpty() || coach.length() > 100 || coach.length() < 3;
    }

    private boolean isTournamentIdInvalid() {
        return tournamentId == null || tournamentId <= 0;
    }

    public void updateDetails(String name, String coach) {
        this.name = name;
        this.coach = coach;
        validate();
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

    private void validateGoals(int goalsFor, int goalsAgainst){
        if(goalsFor < 0 || goalsAgainst < 0){
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

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoach() {
        return coach;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public int getPoints() {
        return points;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getMatchesWin() {
        return matchesWin;
    }

    public int getMatchesDraw() {
        return matchesDraw;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return points == team.points && matchesPlayed == team.matchesPlayed && matchesWin == team.matchesWin && matchesDraw == team.matchesDraw && matchesLost == team.matchesLost && goalsFor == team.goalsFor && goalsAgainst == team.goalsAgainst && goalDifference == team.goalDifference && Objects.equals(id, team.id) && Objects.equals(name, team.name) && Objects.equals(coach, team.coach) && Objects.equals(tournamentId, team.tournamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coach, tournamentId, points, matchesPlayed, matchesWin, matchesDraw, matchesLost, goalsFor, goalsAgainst, goalDifference);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coach='" + coach + '\'' +
                ", tournamentId=" + tournamentId +
                ", points=" + points +
                ", matchesPlayed=" + matchesPlayed +
                ", matchesWin=" + matchesWin +
                ", matchesDraw=" + matchesDraw +
                ", matchesLost=" + matchesLost +
                ", goalsFor=" + goalsFor +
                ", goalsAgainst=" + goalsAgainst +
                ", goalDifference=" + goalDifference +
                '}';
    }
}
