package com.personal.tournament_api.player.domain.exceptions;

import com.personal.tournament_api.shared.domain.enums.DomainErrorType;

public class DuplicatePlayerIdentificationException extends PlayerDomainException {
    public DuplicatePlayerIdentificationException(String identificationNumber) {
        super("Player with identification number '" + identificationNumber + "' already exists",
                DomainErrorType.DUPLICATE_ENTITY);
    }
}
