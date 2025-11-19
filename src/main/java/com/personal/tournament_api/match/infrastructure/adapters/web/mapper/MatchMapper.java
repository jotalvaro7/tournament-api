package com.personal.tournament_api.match.infrastructure.adapters.web.mapper;

import com.personal.tournament_api.match.application.usecases.CreateMatchUseCase;
import com.personal.tournament_api.match.application.usecases.FinishMatchUseCase;
import com.personal.tournament_api.match.application.usecases.UpdateMatchUseCase;
import com.personal.tournament_api.match.domain.model.Match;
import com.personal.tournament_api.match.domain.model.Page;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.FinishMatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchRequestDTO;
import com.personal.tournament_api.match.infrastructure.adapters.web.dto.MatchResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MatchMapper {

    @Mapping(target = "tournamentId", source = "tournamentId")
    @Mapping(target = "homeTeamId", source = "request.homeTeamId")
    @Mapping(target = "awayTeamId", source = "request.awayTeamId")
    @Mapping(target = "matchDate", source = "request.matchDate")
    @Mapping(target = "field", source = "request.field")
    CreateMatchUseCase.CreateMatchCommand toCreateCommand(Long tournamentId, MatchRequestDTO request);

    @Mapping(target = "matchId", source = "matchId")
    @Mapping(target = "matchDate", source = "request.matchDate")
    @Mapping(target = "field", source = "request.field")
    UpdateMatchUseCase.UpdateMatchCommand toUpdateCommand(Long matchId, MatchRequestDTO request);

    @Mapping(target = "matchId", source = "matchId")
    @Mapping(target = "homeTeamScore", source = "request.homeTeamScore")
    @Mapping(target = "awayTeamScore", source = "request.awayTeamScore")
    FinishMatchUseCase.FinishMatchCommand toFinishCommand(Long matchId, FinishMatchRequestDTO request);

    MatchResponseDTO toResponse(Match match);

    List<MatchResponseDTO> toResponseList(List<Match> matches);

    default Page<MatchResponseDTO> toResponsePage(Page<Match> matchPage) {
        List<MatchResponseDTO> matchDTOs = toResponseList(matchPage.getContent());
        return new Page<>(matchDTOs, matchPage.getPage(), matchPage.getSize(), matchPage.getTotalElements());
    }
}
