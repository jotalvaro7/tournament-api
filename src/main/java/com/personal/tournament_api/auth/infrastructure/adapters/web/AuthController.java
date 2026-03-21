package com.personal.tournament_api.auth.infrastructure.adapters.web;

import com.personal.tournament_api.auth.application.usecases.LoginUseCase;
import com.personal.tournament_api.auth.application.usecases.RegisterUserUseCase;
import com.personal.tournament_api.auth.domain.model.User;
import com.personal.tournament_api.auth.domain.enums.UserRole;
import com.personal.tournament_api.auth.infrastructure.adapters.web.dto.AuthResponse;
import com.personal.tournament_api.auth.infrastructure.adapters.web.dto.CreateUserRequest;
import com.personal.tournament_api.auth.infrastructure.adapters.web.dto.LoginRequest;
import com.personal.tournament_api.auth.infrastructure.security.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    private final JwtProvider jwtProvider;

    @PostMapping("/admin/users")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = registerUserUseCase.execute(new RegisterUserUseCase.RegisterUserCommand(
                request.email(),
                request.username(),
                request.password(),
                request.role()
        ));
        String token = jwtProvider.generate(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuthResponse.of(token, user.getEmail(), user.getUsername(), user.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = loginUseCase.execute(new LoginUseCase.LoginCommand(
                request.email(),
                request.password()
        ));
        String token = jwtProvider.generate(user);
        return ResponseEntity.ok(AuthResponse.of(token, user.getEmail(), user.getUsername(), user.getRole()));
    }
}