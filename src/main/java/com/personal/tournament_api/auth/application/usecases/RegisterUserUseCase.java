package com.personal.tournament_api.auth.application.usecases;

import com.personal.tournament_api.auth.domain.enums.UserRole;
import com.personal.tournament_api.auth.domain.model.User;

public interface RegisterUserUseCase {

    User execute(RegisterUserCommand command);

    record RegisterUserCommand(
            String email,
            String username,
            String rawPassword,
            UserRole role
    ) {}
}