package com.personal.tournament_api.tournament.infrastructure.adapters.web.dto;

import com.personal.tournament_api.tournament.domain.enums.StatusTournament;

public record TournamentResponse(
        Long id,
        String name,
        String description,
        StatusTournament status
) {}