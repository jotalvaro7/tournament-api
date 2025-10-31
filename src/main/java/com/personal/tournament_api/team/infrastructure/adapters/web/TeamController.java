package com.personal.tournament_api.team.infrastructure.adapters.web;

import com.personal.tournament_api.team.application.usecases.CreateTeamUseCase;
import com.personal.tournament_api.team.application.usecases.DeleteTeamUseCase;
import com.personal.tournament_api.team.application.usecases.GetTeamUseCase;
import com.personal.tournament_api.team.application.usecases.UpdateTeamUseCase;
import com.personal.tournament_api.team.domain.model.Team;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamRequestDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.dto.TeamResponseDTO;
import com.personal.tournament_api.team.infrastructure.adapters.web.mapper.TeamMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final CreateTeamUseCase createTeamUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final GetTeamUseCase getTeamUseCase;
    private final DeleteTeamUseCase deleteTeamUseCase;
    private final TeamMapper teamMapper;

    @PostMapping
    public ResponseEntity<TeamResponseDTO> create(@Valid @RequestBody TeamRequestDTO request) {
        Team team = createTeamUseCase.create(teamMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toResponse(team));
    }
}
