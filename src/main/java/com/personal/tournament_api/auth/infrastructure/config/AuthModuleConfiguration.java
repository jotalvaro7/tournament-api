package com.personal.tournament_api.auth.infrastructure.config;

import com.personal.tournament_api.auth.application.LoginService;
import com.personal.tournament_api.auth.application.RegisterUserService;
import com.personal.tournament_api.auth.application.usecases.LoginUseCase;
import com.personal.tournament_api.auth.application.usecases.RegisterUserUseCase;
import com.personal.tournament_api.auth.domain.ports.PasswordHasher;
import com.personal.tournament_api.auth.domain.ports.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AuthModuleConfiguration {

    // --- Application Use Cases ---

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new RegisterUserAdapter(new RegisterUserService(userRepository, passwordHasher));
    }

    @Bean
    public LoginUseCase loginUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        return new LoginService(userRepository, passwordHasher);
    }

    // --- Transactional Adapters (infrastructure concern) ---

    static class RegisterUserAdapter implements RegisterUserUseCase {
        private final RegisterUserUseCase delegate;
        RegisterUserAdapter(RegisterUserUseCase delegate) { this.delegate = delegate; }

        @Override @Transactional
        public com.personal.tournament_api.auth.domain.model.User execute(RegisterUserCommand command) {
            return delegate.execute(command);
        }
    }
}