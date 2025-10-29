package com.personal.tournament_api.infrastructure.adapters.in.web.mapper;

import com.personal.tournament_api.application.ports.in.tournament.CreateTournamentUseCase.CreateTournamentCommand;
import com.personal.tournament_api.application.ports.in.tournament.UpdateTournamentUseCase.UpdateTournamentCommand;
import com.personal.tournament_api.domain.model.Tournament;
import com.personal.tournament_api.infrastructure.adapters.in.web.dto.TournamentRequest;
import com.personal.tournament_api.infrastructure.adapters.in.web.dto.TournamentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentMapper {

    TournamentResponse toResponse(Tournament tournament);

    List<TournamentResponse> toResponseList(List<Tournament> tournaments);

    CreateTournamentCommand toCreateCommand(TournamentRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    UpdateTournamentCommand toUpdateCommand(Long id, TournamentRequest request);
}