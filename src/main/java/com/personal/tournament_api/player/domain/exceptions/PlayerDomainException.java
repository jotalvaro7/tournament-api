package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;
import com.personal.tournament_api.shared.domain.exceptions.DomainException;

public class PlayerDomainException extends DomainException {
    public PlayerDomainException(String message, DomainErrorType errorType) {
        super(message, errorType);
    }
}
