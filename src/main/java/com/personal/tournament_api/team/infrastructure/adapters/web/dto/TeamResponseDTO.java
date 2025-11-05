package com.personal.tournament_api.team.infrastructure.adapters.web.dto;

public record TeamResponseDTO(
    Long id,
    String name,
    String coach,
    Long tournamentId,
    Integer points,
    Integer matchesPlayed,
    Integer matchesWin,
    Integer matchesDraw,
    Integer matchesLost,
    Integer goalsFor,
    Integer goalsAgainst,
    Integer goalDifference
) {}
