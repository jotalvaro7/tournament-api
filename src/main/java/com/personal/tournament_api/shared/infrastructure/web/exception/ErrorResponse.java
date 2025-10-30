package com.personal.tournament_api.shared.infrastructure.web.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        List<String> message,
        String path
) {
}