package com.personal.tournament_api.tournament.infrastructure.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TournamentRequest(
        @NotBlank(message = "Tournament name is required")
        @Size(min = 3, max = 100, message = "Tournament name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Tournament description is required")
        @Size(min = 10, max = 500, message = "Tournament description must be between 10 and 500 characters")
        String description
) {}