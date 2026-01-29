package com.personal.tournament_api.player.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.player.application.usecases.CreatePlayerUseCase;
import com.personal.tournament_api.player.application.usecases.UpdatePlayerUseCase;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.infrastructure.adapters.web.dto.PlayerRequestDTO;
import com.personal.tournament_api.player.infrastructure.adapters.web.dto.PlayerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMapper {

    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "identificationNumber", source = "request.identificationNumber")
    @Mapping(target = "teamId", source = "teamId")
    @Mapping(target = "tournamentId", source = "tournamentId")
    CreatePlayerUseCase.CreatePlayerCommand toCreateCommand(Long tournamentId, Long teamId, PlayerRequestDTO request);

    @Mapping(target = "playerId", source = "playerId")
    @Mapping(target = "teamId", source = "teamId")
    @Mapping(target = "tournamentId", source = "tournamentId")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "identificationNumber", source = "request.identificationNumber")
    UpdatePlayerUseCase.UpdatePlayerCommand toUpdateCommand(Long teamId, Long playerId, Long tournamentId, PlayerRequestDTO request);

    PlayerResponseDTO toResponse(Player player);

    List<PlayerResponseDTO> toResponseList(List<Player> players);
}
