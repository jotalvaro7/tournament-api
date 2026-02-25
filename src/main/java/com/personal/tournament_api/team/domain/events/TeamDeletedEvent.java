package com.personal.tournament_api.team.domain.events;

import com.personal.tournament_api.shared.domain.events.DomainEvent;

public record TeamDeletedEvent(Long teamId) implements DomainEvent {
}
