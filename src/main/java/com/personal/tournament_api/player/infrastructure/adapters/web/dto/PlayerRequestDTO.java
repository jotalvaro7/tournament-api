package com.personal.tournament_api.player.infrastructure.adapters.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @NotBlank(message = "Last name is required")
        @Size(max = 70, message = "Last name must not exceed 70 characters")
        String lastName,

        @NotBlank(message = "Identification number is required")
        @Size(max = 20, message = "Identification number must not exceed 20 characters")
        String identificationNumber
) {
}
