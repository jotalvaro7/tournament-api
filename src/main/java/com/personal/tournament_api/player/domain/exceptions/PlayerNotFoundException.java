package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class PlayerNotFoundException extends PlayerDomainException {
    public PlayerNotFoundException(Long playerId) {
        super("Player with id '" + playerId + "' not found", DomainErrorType.NOT_FOUND);
    }
}
