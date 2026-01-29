package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidPlayerNameException extends PlayerDomainException {

    public InvalidPlayerNameException() {
        super("Player name is invalid cannot be null or empty", DomainErrorType.RULE_VIOLATION);
    }
}
