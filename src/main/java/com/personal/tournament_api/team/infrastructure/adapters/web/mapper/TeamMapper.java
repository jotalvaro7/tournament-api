package com.personal.tournament_api.team.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamRequestDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TeamMapper {

    CreateTeamUseCase.CreateTeamCommand toCreateCommand(TeamRequestDTO request);

    TeamResponseDTO toResponse(Team team);
}
