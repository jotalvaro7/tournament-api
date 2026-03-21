package com.personal.tournament_api.auth.application.usecases;

import com.personal.tournament_api.auth.domain.model.User;

public interface LoginUseCase {

    User execute(LoginCommand command);

    record LoginCommand(
            String email,
            String rawPassword
    ) {}
}