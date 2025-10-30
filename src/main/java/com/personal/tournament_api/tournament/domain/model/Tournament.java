package com.personal.tournament_api.tournament.domain.model;

import com.personal.tournament_api.tournament.domain.enums.StatusTournament;
import com.personal.tournament_api.tournament.domain.exceptions.*;

import java.util.Objects;

public class Tournament {
    private Long id;
    private String name;
    private String description;
    private StatusTournament status;

    public Tournament() {
        this.status = StatusTournament.CREATED;
    }

    public Tournament(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = StatusTournament.CREATED;
        validate();
    }

    // --- Domain Rules ---
    public void validate() {
        if (isNameInvalid()) {
            throw new InvalidTournamentNameException();
        }
        if (isDescriptionInvalid()) {
            throw new InvalidTournamentDescriptionException();
        }
    }

    public void updateDetails(String name, String description) {
        this.name = name;
        this.description = description;
        validate();
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
            throw new InvalidTournamentStateException(
                    "Tournament can only be started if it is in created status"
            );
        }
    }

    private void ensureIsInProgress() {
        if (!isInProgress()) {
            throw new InvalidTournamentStateException(
                    "Tournament can only be completed if it is in progress"
            );
        }
    }

    private void ensureIsNotCompleted() {
        if (isCompleted()) {
            throw new InvalidTournamentStateException(
                    "Cannot cancel a completed tournament"
            );
        }
    }

    // --- Boolean State Methods ---
    public boolean isCreated() {
        return this.status == StatusTournament.CREATED;
    }

    public boolean isInProgress() {
        return this.status == StatusTournament.IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this.status == StatusTournament.COMPLETED;
    }


    public boolean isCancelled() {
        return this.status == StatusTournament.CANCELLED;
    }

    public boolean canBeDeleted() {
        return isCreated() || isCancelled() || isCompleted();
    }

    // --- Validation Helpers ---
    private boolean isNameInvalid() {
        return name == null || name.isEmpty();
    }

    private boolean isDescriptionInvalid() {
        return description == null || description.isEmpty();
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTournament getStatus() {
        return status;
    }

    public void setStatus(StatusTournament status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
