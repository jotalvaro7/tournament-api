package com.personal.tournament_api.team.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamRequestDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {

    @Mapping(target = "tournamentId", source = "tournamentId")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "coach", source = "request.coach")
    CreateTeamUseCase.CreateTeamCommand toCreateCommand(Long tournamentId, TeamRequestDTO request);

    TeamResponseDTO toResponse(Team team);
}
