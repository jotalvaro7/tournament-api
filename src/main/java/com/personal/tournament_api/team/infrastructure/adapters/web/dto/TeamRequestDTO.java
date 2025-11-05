package com.personal.tournament_api.team.infrastructure.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamRequestDTO (
    @NotBlank(message = "Team name is required")
    @Size(min = 3, max = 100, message = "Team name must be between 3 and 100 characters")
    String name,
    @NotBlank(message = "Coach name is required")
    @Size(min = 3, max = 100, message = "Coach name must be between 3 and 100 characters")
    String coach
) {}
