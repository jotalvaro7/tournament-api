package com.personal.tournament_api.auth.application;

import com.personal.tournament_api.auth.application.usecases.RegisterUserUseCase;
import com.personal.tournament_api.auth.domain.exceptions.DuplicateEmailException;
import com.personal.tournament_api.auth.domain.model.User;
import com.personal.tournament_api.auth.domain.ports.PasswordHasher;
import com.personal.tournament_api.auth.domain.ports.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterUserService implements RegisterUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterUserService.class);

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public RegisterUserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User execute(RegisterUserCommand command) {
        log.info("Registering user with email: {}", command.email());

        if (userRepository.existsByEmail(command.email())) {
            throw new DuplicateEmailException(command.email());
        }

        String hashedPassword = passwordHasher.hash(command.rawPassword());
        User user = User.create(command.email(), command.username(), hashedPassword, command.role());
        User saved = userRepository.save(user);

        log.info("User registered with id: {}", saved.getId());
        return saved;
    }
}