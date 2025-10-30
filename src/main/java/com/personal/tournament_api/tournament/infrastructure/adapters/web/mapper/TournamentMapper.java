package com.personal.tournament_api.tournament.infrastructure.adapters.web.mapper;


import com.personal.tournament_api.tournament.application.usecases.CreateTournamentUseCase;
import com.personal.tournament_api.tournament.application.usecases.UpdateTournamentUseCase;
import com.personal.tournament_api.tournament.domain.model.Tournament;
import com.personal.tournament_api.tournament.infrastructure.adapters.web.dto.TournamentRequest;
import com.personal.tournament_api.tournament.infrastructure.adapters.web.dto.TournamentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentMapper {

    TournamentResponse toResponse(Tournament tournament);

    List<TournamentResponse> toResponseList(List<Tournament> tournaments);

    CreateTournamentUseCase.CreateTournamentCommand toCreateCommand(TournamentRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    UpdateTournamentUseCase.UpdateTournamentCommand toUpdateCommand(Long id, TournamentRequest request);
}