package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidPlayerLastNameException extends  PlayerDomainException {

    public InvalidPlayerLastNameException() {
        super("The last name of the player is invalid, cannot be null or empty", DomainErrorType.RULE_VIOLATION);
    }
}
