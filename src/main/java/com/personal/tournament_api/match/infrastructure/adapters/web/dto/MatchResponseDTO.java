package com.personal.tournament_api.match.infrastructure.adapters.web.dto;

import com.personal.tournament_api.match.domain.model.MatchStatus;

import java.time.LocalDateTime;

public record MatchResponseDTO(
        Long id,
        Long tournamentId,
        Long homeTeamId,
        Long awayTeamId,
        Integer homeTeamScore,
        Integer awayTeamScore,
        LocalDateTime matchDate,
        String field,
        MatchStatus status
) {}
