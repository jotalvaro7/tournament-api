package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class InvalidPlayerIdentificationNumberException extends PlayerDomainException {

    public InvalidPlayerIdentificationNumberException() {
        super("The identification number of the player is invalid, cannot be null or empty", DomainErrorType.RULE_VIOLATION);
    }
}
