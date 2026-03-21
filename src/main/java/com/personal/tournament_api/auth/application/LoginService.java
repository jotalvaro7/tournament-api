package com.personal.tournament_api.auth.application;

import com.personal.tournament_api.auth.application.usecases.LoginUseCase;
import com.personal.tournament_api.auth.domain.exceptions.InvalidCredentialsException;
import com.personal.tournament_api.auth.domain.model.User;
import com.personal.tournament_api.auth.domain.ports.PasswordHasher;
import com.personal.tournament_api.auth.domain.ports.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService implements LoginUseCase {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public LoginService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User execute(LoginCommand command) {
        log.info("Login attempt for email: {}", command.email());

        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.rawPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        log.info("User '{}' authenticated successfully", command.email());
        return user;
    }
}