package com.personal.tournament_api.tournament.domain.events;

import com.personal.tournament_api.shared.domain.events.DomainEvent;

public record TournamentDeletedEvent(Long tournamentId) implements DomainEvent {
}
