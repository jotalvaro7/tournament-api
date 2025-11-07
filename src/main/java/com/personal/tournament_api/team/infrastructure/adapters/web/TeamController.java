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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments/{tournamentId}/teams")
@RequiredArgsConstructor
public class TeamController {

    private final CreateTeamUseCase createTeamUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final GetTeamUseCase getTeamUseCase;
    private final DeleteTeamUseCase deleteTeamUseCase;
    private final TeamMapper teamMapper;

    @PostMapping
    public ResponseEntity<TeamResponseDTO> create(@PathVariable Long tournamentId,
                                                  @Valid @RequestBody TeamRequestDTO request) {
        Team team = createTeamUseCase.create(teamMapper.toCreateCommand(tournamentId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toResponse(team));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDTO> getById(@PathVariable Long teamId) {
        return getTeamUseCase.getById(teamId)
                .map(team -> ResponseEntity.ok(teamMapper.toResponse(team)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> getAllOrderByNameAsc(@PathVariable Long tournamentId) {
        List<Team> teams = getTeamUseCase.getAllByTournamentIdOrderByNameAsc(tournamentId);
        return ResponseEntity.ok(teamMapper.toResponseList(teams));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponseDTO> update(@PathVariable Long teamId,
                                                      @Valid @RequestBody TeamRequestDTO request) {
        Team team = updateTeamUseCase.update(teamMapper.toUpdateCommand(teamId, request));
        return ResponseEntity.ok(teamMapper.toResponse(team));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable Long teamId) {
        deleteTeamUseCase.delete(teamId);
        return ResponseEntity.noContent().build();
    }

}
