package com.personal.tournament_api.player.infrastructure.adapters.web;

import com.personal.tournament_api.player.application.usecases.CreatePlayerUseCase;
import com.personal.tournament_api.player.application.usecases.GetPlayerByIdUseCase;
import com.personal.tournament_api.player.application.usecases.GetPlayersByTeamUseCase;
import com.personal.tournament_api.player.domain.model.Player;
import com.personal.tournament_api.player.infrastructure.adapters.web.dto.PlayerRequestDTO;
import com.personal.tournament_api.player.infrastructure.adapters.web.dto.PlayerResponseDTO;
import com.personal.tournament_api.player.infrastructure.adapters.web.mapper.PlayerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments/{tournamentId}/teams/{teamId}/players")
@RequiredArgsConstructor
public class PlayerController {

    private final CreatePlayerUseCase createPlayerUseCase;
    private final GetPlayersByTeamUseCase getPlayersByTeamUseCase;
    private final GetPlayerByIdUseCase getPlayerByIdUseCase;
    private final PlayerMapper playerMapper;

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> create(@PathVariable Long tournamentId,
                                                    @PathVariable Long teamId,
                                                    @Valid @RequestBody PlayerRequestDTO request) {

        var command = playerMapper.toCreateCommand(tournamentId, teamId, request);
        Player player = createPlayerUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.toResponse(player));
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> getPlayers(@PathVariable Long teamId) {
        List<Player> players = getPlayersByTeamUseCase.getPlayersByTeamId(teamId);
        return ResponseEntity.ok(playerMapper.toResponseList(players));
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponseDTO> getPlayerById(@PathVariable Long teamId,
                                                           @PathVariable Long playerId) {
        return getPlayerByIdUseCase.getPlayerById(teamId, playerId)
                .map(player -> ResponseEntity.ok(playerMapper.toResponse(player)))
                .orElse(ResponseEntity.notFound().build());

    }
}
