package com.personal.tournament_api.tournament.domain.model;

import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.*;
import com.personal.tournament_api.tournament.domain.model.vo.TournamentDescription;
import com.personal.tournament_api.tournament.domain.model.vo.TournamentName;

import java.util.Objects;

public class Tournament {

    private final Long id;
    private TournamentName name;
    private TournamentDescription description;
    private StatusTournament status;

    private Tournament(Long id, TournamentName name, TournamentDescription description, StatusTournament status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // --- Factory Methods ---

    public static Tournament create(String name, String description) {
        return new Tournament(
                null,
                new TournamentName(name),
                new TournamentDescription(description),
                StatusTournament.CREATED
        );
    }

    public static Tournament reconstitute(Long id, String name, String description, StatusTournament status) {
        return new Tournament(
                id,
                new TournamentName(name),
                new TournamentDescription(description),
                status
        );
    }

    // --- Domain Behavior ---

    public void updateDetails(String name, String description) {
        this.name = new TournamentName(name);
        this.description = new TournamentDescription(description);
    }

    public void startTournament() {
        ensureIsCreated();
        this.status = StatusTournament.IN_PROGRESS;
    }

    public void endTournament() {
        ensureIsInProgress();
        this.status = StatusTournament.COMPLETED;
    }

    public void cancelTournament() {
        ensureIsNotCompleted();
        this.status = StatusTournament.CANCELLED;
    }

    public void validateIfCanBeDeleted() {
        if (!canBeDeleted()) {
            throw new TournamentCannotBeDeletedException();
        }
    }

    // --- Guard Clauses ---

    private void ensureIsCreated() {
        if (!isCreated()) {
            throw new InvalidTournamentStateException("Tournament can only be started if it is in created status");
        }
    }

    private void ensureIsInProgress() {
        if (!isInProgress()) {
            throw new InvalidTournamentStateException("Tournament can only be completed if it is in progress");
        }
    }

    private void ensureIsNotCompleted() {
        if (isCompleted()) {
            throw new InvalidTournamentStateException("Cannot cancel a completed tournament");
        }
    }

    // --- State Queries ---

    public boolean isCreated() { return this.status == StatusTournament.CREATED; }
    public boolean isInProgress() { return this.status == StatusTournament.IN_PROGRESS; }
    public boolean isCompleted() { return this.status == StatusTournament.COMPLETED; }
    public boolean isCancelled() { return this.status == StatusTournament.CANCELLED; }
    public boolean canBeDeleted() { return isCreated() || isCancelled() || isCompleted(); }

    // --- Getters ---

    public Long getId() { return id; }
    public String getName() { return name.value(); }
    public String getDescription() { return description.value(); }
    public StatusTournament getStatus() { return status; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(getName(), that.getName()) &&
               Objects.equals(getDescription(), that.getDescription()) &&
               status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getDescription(), status);
    }

    @Override
    public String toString() {
        return "Tournament{id=" + id + ", name='" + getName() + "', description='" + getDescription() + "', status=" + status + '}';
    }
}