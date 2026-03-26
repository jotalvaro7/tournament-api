package com.personal.tournament_api.auth.infrastructure.config;

import com.personal.tournament_api.auth.application.usecases.RegisterUserUseCase;
import com.personal.tournament_api.auth.domain.enums.UserRole;
import com.personal.tournament_api.auth.domain.ports.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String ADMIN_EMAIL = "admin@tournament.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private final UserRepository userRepository;
    private final RegisterUserUseCase registerUserUseCase;

    public DataInitializer(UserRepository userRepository, RegisterUserUseCase registerUserUseCase) {
        this.userRepository = userRepository;
        this.registerUserUseCase = registerUserUseCase;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
            registerUserUseCase.execute(new RegisterUserUseCase.RegisterUserCommand(
                    ADMIN_EMAIL,
                    ADMIN_USERNAME,
                    ADMIN_PASSWORD,
                    UserRole.ADMIN
            ));
            log.info("Default admin user created: {}", ADMIN_EMAIL);
        } else {
            log.info("Admin user already exists, skipping initialization");
        }
    }
}
