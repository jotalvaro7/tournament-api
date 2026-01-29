package com.personal.tournament_api.player.infrastructure.adapters.web.dto;

public record PlayerResponseDTO(
        Long id,
        String name,
        String lastName,
        String identificationNumber,
        Long teamId
) {
}
