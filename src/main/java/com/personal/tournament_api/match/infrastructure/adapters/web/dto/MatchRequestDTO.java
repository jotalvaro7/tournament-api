package com.personal.tournament_api.match.infrastructure.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record MatchRequestDTO(
        @NotNull(message = "Home team ID is required")
        @Positive(message = "Home team ID must be positive")
        Long homeTeamId,

        @NotNull(message = "Away team ID is required")
        @Positive(message = "Away team ID must be positive")
        Long awayTeamId,

        @NotNull(message = "Match date is required")
        LocalDateTime matchDate,

        @NotBlank(message = "Field is required")
        @Size(max = 100, message = "Field name cannot exceed 100 characters")
        String field
) {}
