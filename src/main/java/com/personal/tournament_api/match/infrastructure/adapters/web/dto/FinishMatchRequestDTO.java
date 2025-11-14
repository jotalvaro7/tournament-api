package com.personal.tournament_api.match.infrastructure.adapters.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record FinishMatchRequestDTO(
        @NotNull(message = "Home team score is required")
        @PositiveOrZero(message = "Home team score cannot be negative")
        Integer homeTeamScore,

        @NotNull(message = "Away team score is required")
        @PositiveOrZero(message = "Away team score cannot be negative")
        Integer awayTeamScore
) {}
