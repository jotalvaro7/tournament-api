package com.personal.tournament_api.shared.domain.ports;

import com.personal.tournament_api.shared.domain.events.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
