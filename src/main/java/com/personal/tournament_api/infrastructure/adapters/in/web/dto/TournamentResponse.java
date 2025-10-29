package com.personal.tournament_api.infrastructure.adapters.in.web.dto;

import com.personal.tournament_api.domain.enums.StatusTournament;

public record TournamentResponse(
        Long id,
        String name,
        String description,
        StatusTournament status
) {}